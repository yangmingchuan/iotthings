package com.ymc.iotthings.webserver.sendserver;

import com.ymc.iotthings.webserver.HeartBeatHandler;
import com.ymc.iotthings.webserver.MQSender;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * package name: com.vip.things.netty.webserver
 * date :2019/3/28
 * author : ymc
 **/

@Component
public class WebSendServer {
    /**
     * NettyServerListener 日志输出器
     */
    private static final Logger LOG = LoggerFactory.getLogger(WebSendServer.class);

    @Resource
    MQSender mqSender;

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("http-codec", new HttpServerCodec()); // Http消息编码解码
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536)); // Http消息组装
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler()); // WebSocket通信支持
                            pipeline.addLast("handler", new WebSendServerHandler(mqSender)); // WebSocket服务端Handler
                            // 添加心跳检测
                            pipeline.addLast(new IdleStateHandler(10,
                                    0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new HeartBeatHandler());
                        }
                    });
            Channel channel = bootstrap.bind(port).sync().channel();
            LOG.info("WebSocket 已经启动，端口：" + port + ".");
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

//    public static void main(String[] args) throws Exception {
//        WebSocketServer.run(Init.PORT);
//    }

}
