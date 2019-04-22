package com.ymc.iotthings.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * mq
 *
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-10-22 14:37
 */

@Component
public class MQSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQSender.class);

    @Resource
    private AmqpTemplate amqpTemplate;

    public MQSender() {
        LOGGER.info("MQSender 初始化中...");
    }

    public void send(String content) {
//        Message message = MessageBuilder.withBody(content.getBytes())
//                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
//                .setContentEncoding("utf-8")
//                .build();
        LOGGER.info("send -> {} -> {}","hello", content);
        amqpTemplate.convertAndSend("hello",content);
    }
}
