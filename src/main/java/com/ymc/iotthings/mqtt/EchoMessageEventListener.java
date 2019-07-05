package com.ymc.iotthings.mqtt;

import com.ymc.iotthings.mqtt.channel.WrappedChannel;
import com.ymc.iotthings.mqtt.listener.DefaultMqttMessageEventListener;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件 分发监听
 *
 * @author ymc
 * @date 2019年7月5日 11:18:44
 */
public class EchoMessageEventListener extends DefaultMqttMessageEventListener {
    private static final Logger logger = LoggerFactory.getLogger(EchoMessageEventListener.class);

    @Override
    public void publish(WrappedChannel channel, MqttPublishMessage msg) {
        String topic = msg.variableHeader().topicName();
        ByteBuf buf = msg.content().duplicate();
        byte[] tmp = new byte[buf.readableBytes()];
        buf.readBytes(tmp);
//        String content = new String(tmp);
//        MqttPublishMessage sendMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
////                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, false, 0),
////                new MqttPublishVariableHeader(topic, 0),
////                Unpooled.buffer().writeBytes(new String(content.toUpperCase()).getBytes()));
////        channel.writeAndFlush(sendMessage);
    }
}
