package com.ymc.iotthings.webserver.testclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

import static com.ymc.iotthings.webserver.beanutils.Init.PORT;

/**
 * package name: com.ymc.iotthings.webserver.testclient
 * date :2019/5/13
 * author : ymc
 **/

public class NettyClient {
    private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

    @Value("${printer.server.host}")
    private String host;
    @Value("${printer.server.port}")
    private int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer(host, port));
            ChannelFuture f = b.connect(host, port);
            //断线重连
            f.addListener((ChannelFutureListener) channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    final EventLoop loop = channelFuture.channel().eventLoop();
                    loop.schedule(() -> {
                        LOG.error("服务端链接不上，开始重连操作...");
                        start();
                    }, 1L, TimeUnit.SECONDS);
                } else {
                    Channel channel = channelFuture.channel();
                    LOG.info("服务端链接成功...");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for(int i=0 ;i<1;i++){
            new NettyClient("127.0.0.1", PORT).start();
        }

    }


}
