package com.ymc.iotthings.webserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * web socket 自定义 handler
 * package name: com.vip.things.netty.webserver
 * date :2019/3/28
 * author : ymc
 **/

@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServerHandler.class);
    LinkedList<Channel> clients = new LinkedList<>();

    HashMap<Integer,LinkedList<Channel>> clientLists = new HashMap<>();

    private WebSocketServerHandshaker handshaker;
    private MQSender mqSender;

    public static final byte PING_MSG = 1;
    public static final byte PONG_MSG = 2;
    public static final byte CUSTOM_MSG = 3;
    protected String name;
    private int heartbeatCount = 0;

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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // WebSocket接入
        else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        clients.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        clients.remove(ctx.channel());
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req)
            throws Exception {
        // 如果HTTP解码失败，返回HHTP异常
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        LOG.error(clients.toString());
        // 判断是否有权限，即 请求url 中有没有传递指定的参数
        Map<String, String> parmMap = new RequestParser(req).parse();
        if (parmMap.get("id").equals("10")){
            if(clientLists.size() == 0){
                LinkedList<Channel> clientListBean = new LinkedList<>();
                clientListBean.add(clients.getLast());
                clientLists.put(Integer.valueOf(parmMap.get("id")),clientListBean);
            }else{
                // 如果有数据判断后 加入
                for (Map.Entry<Integer, LinkedList<Channel>> entry : clientLists.entrySet()) {
                    if(entry.getKey().equals(parmMap.get("id"))){
                        for(Channel channelBean : entry.getValue()){
                            if(!channelBean.id().equals(String.valueOf(ctx.channel().id()))){
                                entry.getValue().add(clients.getLast());
                            }
                        }
                    }
                }
            }
        }else if(parmMap.get("id").equals("1")){

        }else{
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.UNAUTHORIZED));
        }

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
        mqSender.send(request);

        if(request.contains("A")){
            // 发送到 客户端
            ctx.writeAndFlush(new TextWebSocketFrame("发送到 客户端A: "+ msg));
        }else{
            // 发送到 客户端
            ctx.writeAndFlush(new TextWebSocketFrame("发送到 客户端B: "+ msg));
        }


    }

}
