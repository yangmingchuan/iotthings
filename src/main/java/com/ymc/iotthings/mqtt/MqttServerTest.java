package com.ymc.iotthings.mqtt;

import com.ymc.iotthings.mqtt.channel.WrappedChannel;
import com.ymc.iotthings.mqtt.pojo.MqttRequest;
import com.ymc.iotthings.mqtt.server.Server;
import com.ymc.iotthings.mqtt.server.SocketType;
import com.ymc.iotthings.webserver.rabbitmq.MQSender;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 18:41
 */
@Component
public class MqttServerTest {

    private static final Logger logger = LoggerFactory.getLogger(MqttServerTest.class);
    private static Server server;
    private static String pubTopic = "PRODUCT/S/670ff505456807867063934/S_Data";
    @Resource
    MQSender mqSender;

    public void run() throws Exception {
        server = new Server();
        server.setPort(1883);
        server.setOpenCount(true);
        server.setCheckHeartbeat(true);
        server.setOpenStatus(true);
        server.setMqttTransferListener((ctx, msg) -> {
//            MqttRequest mqttRequest = new MqttRequest((msg.toString().getBytes()));
            if (msg instanceof MqttPublishMessage) {
                for (WrappedChannel channel : server.getChannels().values()) {
                    ByteBuf buf = ((MqttPublishMessage) msg).content().duplicate();
                    byte[] tmp = new byte[buf.readableBytes()];
                    buf.readBytes(tmp);
                    try {
//                        MqttRequest ss = new MqttRequest(HexUtils.hexStringToBytes("68 02 00 00 00 00 00 00"));
//                        logger.info("服务端发送消息. topic={},localAddress = {},chanelId = {}, byteMessage={}.",
//                                ((MqttPublishMessage) msg).variableHeader().topicName(),channel.localAddress(),channel.id(), Arrays.toString(tmp));
//                        server.send(channel, "PRODUCT/S/670ff505456807867063934/S_Data", ss);
                        // ((MqttPublishMessage) msg).variableHeader().topicName()

                        logger.info("服务端发送消息. topic={},localAddress = {},chanelId = {}, byteMessage={}.",
                                ((MqttPublishMessage) msg).variableHeader().topicName(), channel.localAddress(),
                                channel.id(), Arrays.toString(tmp));
                        mqSender.webSend(Arrays.toString(tmp));
                        MqttRequest ss = new MqttRequest(tmp);
                        server.send(channel, ((MqttPublishMessage) msg).variableHeader().topicName(), ss);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        server.addEventListener(new EchoMessageEventListener());
        server.setSocketType(SocketType.MQTT);
        server.bind();
    }


//        Thread.sleep(10000L);
//        //模拟推送
//        MqttRequest mqttRequest = new MqttRequest(HexUtils.hexStringToBytes("68 02 00 00 00 00 00 00"));
//        while (true) {
//            if (server.getChannels().size() > 0) {
//                for (WrappedChannel channel : server.getChannels().values()) {
//                    logger.info("推送消息 mqttRequest = {}",mqttRequest);
//                    server.send(channel, "PRODUCT/S/670ff505456807867063934/S_Data", mqttRequest);
//                }
//            }
//            Thread.sleep(10000L);
//        }


}
