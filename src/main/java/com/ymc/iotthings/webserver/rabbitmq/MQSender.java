package com.ymc.iotthings.webserver.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private final AmqpAdmin amqpAdmin;

    private final RabbitTemplate rabbitTemplate;

    private final FanoutExchange fanoutExchange;

    @Autowired
    public MQSender(AmqpAdmin amqpAdmin, RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        LOGGER.info("MQSender 初始化中...");
        this.amqpAdmin = amqpAdmin;
        this.rabbitTemplate = rabbitTemplate;
        this.fanoutExchange = fanoutExchange;
    }

    /**
     * 给queue发送消息
     * @param queueName queueName
     * @param msg msg
     */
    public void send(String queueName,String msg){
        Queue queue = new Queue(queueName);
        addQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(fanoutExchange);
        amqpAdmin.declareBinding(binding);
        MessageProperties messageProperties = new MessageProperties();
        //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        Message message = new Message(msg.getBytes(),messageProperties);
        rabbitTemplate.convertAndSend(fanoutExchange.getName(),queueName,message);
    }


    /**
     * 创建一个指定的Queue
     * @return queueName
     */
    private void addQueue(Queue queue){
        amqpAdmin.declareQueue(queue);
    }

    /**
     * 删除一个queue
     * @param queueName queueName
     */
    public boolean deleteQueue(String queueName){
        return amqpAdmin.deleteQueue(queueName);
    }


}
