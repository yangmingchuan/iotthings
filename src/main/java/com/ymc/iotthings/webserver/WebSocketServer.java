package com.ymc.iotthings.webserver;

import com.ymc.iotthings.webserver.beanutils.Init;
import com.ymc.iotthings.webserver.rabbitmq.MQSender;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * netty client server 端
 *
 * package name: com.vip.things.netty.webserver
 * date :2019/3/28
 * author : ymc
 **/

@Component
public class WebSocketServer {
    /**
     * NettyServerListener 日志输出器
     */
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    @Resource
    MQSender mqSender;

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 保持长连接
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("http-codec", new HttpServerCodec()); // Http消息编码解码
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536)); // Http消息组装
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler()); // WebSocket通信支持
                            pipeline.addLast("handler", new WebSocketServerHandler(mqSender)); // WebSocket服务端Handler
                            //服务端心跳检测
                            pipeline.addLast(new IdleStateHandler(Init.SERVER_READ_IDEL_TIME_OUT,
                                    Init.SERVER_WRITE_IDEL_TIME_OUT,Init.SERVER_ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
                            //粘包拆包处理
                            ByteBuf delimiter = Unpooled.copiedBuffer("&&&".getBytes());
                            /*
                             * 解码的帧的最大长度为：2048
                             * 解码时是否去掉分隔符：false
                             * 解码分隔符每次传输都以该字符结尾：&&&
                             */
                            pipeline.addLast(new DelimiterBasedFrameDecoder(2048,false,delimiter));
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                        }
                    });
            Channel channel = bootstrap.bind(port).sync().channel();
            LOG.info("clientSocket 已经启动，端口：" + port + ".");
            channel.closeFuture().sync();
        } finally {
            // 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
