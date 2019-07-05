package com.ymc.iotthings.mqtt.server;

import com.ymc.iotthings.mqtt.channel.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:47
 */
public class ServerDispatchHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ServerDispatchHandler.class);
    private MqttTransferListener mqttTransferListener;
    protected EventDispatcher eventDispatcher;

    public ServerDispatchHandler(EventDispatcher eventDispatcher, MqttTransferListener mqttTransferListener) {
        if (eventDispatcher == null) {
            throw new IllegalArgumentException("eventDispatcher cannot be null.");
        }
        this.mqttTransferListener = mqttTransferListener;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String channelId = ctx.channel().id().asShortText();
        if(msg instanceof MqttPublishMessage){
            logger.info("Handler消息. topic={}, ctxId={}.",
                    ((MqttPublishMessage) msg).variableHeader().topicName(),ctx.channel().id());
            mqttTransferListener.transMessage(ctx,msg);
        }
        WrappedChannel channel = ((Server) eventDispatcher.getService()).getChannel(channelId);
        eventDispatcher.dispatchMessageEvent(ctx, channel, msg);
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        WrappedChannel channel = new WrappedChannel(ctx.channel());
        String channelId = channel.id().asShortText();
        if (logger.isDebugEnabled()) {
            logger.debug("设备连接 '{}'.", channelId);
        }
        ((Server) eventDispatcher.getService()).getChannels().put(channelId, channel);
        eventDispatcher.dispatchChannelEvent(ctx, channel);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        closeChannel(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String channelId = ctx.channel().id().asShortText();
        WrappedChannel channel = ((Server) eventDispatcher.getService()).getChannel(channelId);
        if (channel != null) {
            eventDispatcher.dispatchExceptionCaught(ctx, channel, cause);
        }

        // 处理IOException，主动关闭channel
        if (cause instanceof IOException) {
            ctx.close();
            closeChannel(ctx);
        }
    }

    private void closeChannel(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asShortText();
        WrappedChannel channel = ((Server) eventDispatcher.getService()).getChannels().remove(channelId);
        if (channel != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("设备 id： '{}' 断开.", channelId);
            }

            eventDispatcher.dispatchChannelEvent(ctx, channel);
        }
        ctx.close();
    }

}