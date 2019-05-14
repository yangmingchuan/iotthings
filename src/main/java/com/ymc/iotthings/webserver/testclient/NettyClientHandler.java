package com.ymc.iotthings.webserver.testclient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 客户端 handler
 *
 * package name: com.ymc.iotthings.webserver.testclient
 * date :2019/5/13
 * author : ymc
 **/

public class NettyClientHandler extends SimpleChannelInboundHandler {

    private static final Logger LOG = LoggerFactory.getLogger(NettyClientHandler.class);

    private String host;
    private int port;
    private NettyClient nettyClinet;
    private String tenantId;


    public NettyClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        nettyClinet = new NettyClient(host, port);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        LOG.error("服务端 说" + o.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.error("通道已连接！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.error("断线了...");
        //使用过程中断线重连
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> nettyClinet.start(), 1, TimeUnit.SECONDS);
        ctx.fireChannelInactive();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                LOG.error("READER_IDLE!");
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                /**发送心跳,保持长连接*/
                String s = "ping$$$";
                ctx.channel().writeAndFlush(s);
                LOG.error("心跳发送成功!");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                LOG.error("ALL_IDLE!");
            }
        }
        super.userEventTriggered(ctx, evt);
    }


}
