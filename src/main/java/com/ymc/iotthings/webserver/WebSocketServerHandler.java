package com.ymc.iotthings.webserver;

import com.ymc.iotthings.webserver.beanutils.ChannelBean;
import com.ymc.iotthings.webserver.beanutils.Init;
import com.ymc.iotthings.webserver.beanutils.RequestParser;
import com.ymc.iotthings.webserver.rabbitmq.MQSender;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * web socket 自定义 handler
 *
 * package name: com.vip.things.netty.webserver
 * date :2019/3/28
 * author : ymc
 **/

@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServerHandler.class);
    /**
     * 线程安全 linkedList
     */
    private static ConcurrentLinkedQueue<ChannelBean> beanList = new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<ChannelHandlerContext> ctList = new ConcurrentLinkedQueue<>();
    private WebSocketServerHandshaker handshaker;
    private MQSender mqSender;
    protected String name;
    /**
     * 心跳断开次数
     */
    private int heartCounter = 0;

    public WebSocketServerHandler(MQSender mqSender) {
        this.mqSender = mqSender;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 用户状态监听
     * @param ctx ChannelHandlerContext
     * @param evt Object
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)){
                // 空闲10s之后触发 (心跳包丢失)
                if (heartCounter >= 3) {
                    // 连续丢失3个心跳包 (断开连接)
                    ctx.channel().close().sync();
                    LOG.error("已与"+ctx.channel().remoteAddress()+"断开连接");
                } else {
                    heartCounter++;
                    LOG.debug(ctx.channel().remoteAddress() + "丢失了第 " + heartCounter + " 个心跳包");
                }
            }

        }
    }

    /**
     * 通道信息的读取
     * @param ctx ChannelHandlerContext
     * @param msg msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        heartCounter = 0;
        LOG.error("-- channelRead0 --"+msg.toString() );
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // WebSocket接入
        else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    /**
     * 设备连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctList.add(ctx);
        LOG.error("-- channelActive --"+ctx.toString() );
    }

    /**
     * 设备断开
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        beanList.removeIf(channelBean -> channelBean.getChannelId().equals(ctx.channel().id()));
        ctList.remove(ctx);
        LOG.error("-- remove --" + beanList.toString());
    }

    private ChannelBean channelBean;

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req)
            throws Exception {
        // 如果HTTP解码失败，返回HHTP异常
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        // 判断是否有权限，即 请求url 中有没有传递指定的参数
        Map<String, String> parmMap = new RequestParser(req).parse();
        if (parmMap.get("id").equals("10") || parmMap.get("id").equals("1") || parmMap.get("id").equals("2")) {
            channelBean = new ChannelBean();
            channelBean.setLineId(Integer.valueOf(parmMap.get("id")));
            channelBean.setChannelId(ctx.channel().id());
            channelBean.setActive(ctx.channel().isActive());
            if (beanList.size() == 0 || !beanList.contains(channelBean)) {
                beanList.add(channelBean);
            }
        } else {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.UNAUTHORIZED));
        }
        LOG.error(beanList.toString());
        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory
                (Init.WEB_SOCKET_URL, null, false);

        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
        LOG.info("设备连接：" + ctx.channel().toString());
    }

    /**
     * 如果状态不对 返回 http 应答
     *
     * @param ctx ChannelHandlerContext
     * @param req FullHttpRequest
     * @param res FullHttpResponse
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res, res.content().readableBytes());
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        LOG.info(String.format("%s socketServer 接收到的消息 %s", ctx.channel(), request));
        String msg = String.format("%s  %s", LocalDateTime.now().
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), request);

//        for (ChannelBean bean : beanList) {
//            if (bean.isActive() && bean.getChannelId().equals(ctx.channel().id())) {
//                ctx.writeAndFlush(new TextWebSocketFrame("发送到 客户端 -" + bean.getLineId() + "- :" + msg));
//                mqSender.send("exchange."+bean.getLineId(),bean);
//            }
//        }

    }

    @RabbitListener(queues = "#{queueMessages.name}")
    public void processMessage(String content){
        System.out.println("WebSocketServerHandler receiver web bean :" + content);

        //TODO 2019年7月5日 17:20:42
        for(ChannelHandlerContext ctBean : ctList){
            if(ctBean.isRemoved()){
                continue;
            }
            ctBean.writeAndFlush(new TextWebSocketFrame("设备信息 -" + content));
        }

    }

}
