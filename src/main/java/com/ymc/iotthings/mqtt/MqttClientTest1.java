package com.ymc.iotthings.mqtt;

import com.ymc.iotthings.util.HexUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 控制端
 *
 * @author daoshenzzg@163.com
 * @date 2018/12/30 18:40
 */
@Component
public class MqttClientTest1 {
    private static final Logger logger = LoggerFactory.getLogger(MqttServerTest.class);
    private static boolean hasfirs = false;

    public void run() throws Exception {
        final String broker = "tcp://127.0.0.1:1883";
        final String clientId = "GID_11";
        final String[] topic = {"PRODUCT/P/#"};
        final String[] pubTop = {"PRODUCT/P/1"};
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            final MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            logger.info("Connecting to broker: {}", broker);
            connOpts.setServerURIs(new String[]{broker});
            connOpts.setUserName("TEST");
            connOpts.setPassword("TEST".toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setConnectionTimeout(10);
            connOpts.setKeepAliveInterval(10);
            connOpts.setAutomaticReconnect(true);
            connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            sampleClient.setCallback(new MqttCallbackExtended() {
                public void connectComplete(boolean reconnect, String serverURI) {
                    logger.info("客户端连接成功");
                    try {
                        sampleClient.subscribe(topic[0], 0);
                    } catch (Exception ex) {
                        logger.error("MqttCallbackExtended 错误 = {}", ex.getMessage());
                    }
                }

                public void connectionLost(Throwable throwable) {
                    logger.error("服务器连接丢失", throwable);
                }

                public void messageArrived(String topic, MqttMessage mqttMessage) {
                    pubTop[0] = topic.replace("/P", "/S");
                    logger.info("控制端接收消息. topic={}, message={}.", topic, new String(mqttMessage.getPayload()));
                    hasfirs = true;
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    logger.info("通信完成. messageId={}.", iMqttDeliveryToken.getMessageId());
                }
            });
            sampleClient.connect(connOpts);

            while (true) {
                try {
                    if (hasfirs) {
                        final MqttMessage message = new MqttMessage(HexUtils.hexStringToBytes("68 02 00 00 00 00 00 00"));
                        message.setQos(0);
                        logger.info("控制端发送消息. pubTop={}, message={}.", pubTop, message);
                        sampleClient.publish("PRODUCT/S/670ff505456807867063934/S_Data", message);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Thread.sleep(20000L);
            }
        } catch (Exception me) {
            me.printStackTrace();
        }
    }


}
