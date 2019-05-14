package com.ymc.iotthings.webserver.testclient;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 客户端
 *
 * package name: com.ymc.iotthings.webserver.testclient
 * date :2019/5/13
 * author : ymc
 **/

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private String host;
    private int port;

    public ClientChannelInitializer( String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //解决TCP粘包拆包的问题，以特定的字符结尾（$$$）
        pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Unpooled.copiedBuffer("$$$".getBytes())));
        //字符串解码和编码
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        //心跳检测
        pipeline.addLast(new IdleStateHandler(0,10,0, TimeUnit.SECONDS));
        //客户端的逻辑
        pipeline.addLast("handler", new NettyClientHandler(host,port));

    }
}