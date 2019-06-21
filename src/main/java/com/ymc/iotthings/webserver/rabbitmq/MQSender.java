package com.ymc.iotthings.webserver.rabbitmq;

import com.ymc.iotthings.webserver.beanutils.ChannelBean;
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
//    private final FanoutExchange fanoutExchange;
    private final RabbitmqConfig rabbitmqConfig;

    @Autowired
    public MQSender(AmqpAdmin amqpAdmin, RabbitTemplate rabbitTemplate, RabbitmqConfig rabbitmqConfig) {
        LOGGER.info("MQSender 初始化中...");
        this.amqpAdmin = amqpAdmin;
        this.rabbitTemplate = rabbitTemplate;
//        this.fanoutExchange = fanoutExchange;
        this.rabbitmqConfig = rabbitmqConfig;
    }

    /**
     * 给queue发送消息
     * @param exchangeName queueName
     * @param msg msg
     */
    public <T> void send(String exchangeName,T msg){
//        FanoutExchange exchange = new FanoutExchange(exchangeName);
//        amqpAdmin.declareExchange(exchange);
        Queue queue = rabbitmqConfig.queueMessage();
        amqpAdmin.declareQueue(queue);
//        Binding binding = BindingBuilder.bind(queue).to(fanoutExchange);
//        amqpAdmin.declareBinding(binding);
        if(msg instanceof String){
            MessageProperties messageProperties = new MessageProperties();
            //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            Message message = new Message(((String) msg).getBytes(),messageProperties);
            rabbitTemplate.convertAndSend("exchange",queue.getName(),message);
        }else if(msg instanceof ChannelBean){
            rabbitTemplate.convertAndSend("exchange",queue.getName(), msg);
        }
    }

    /**
     * 给queue发送消息
     * @param msg msg
     */
    public <T> void webSend(T msg){
        Queue queue = rabbitmqConfig.queueMessages();
        amqpAdmin.declareQueue(queue);
//        Binding binding = BindingBuilder.bind(queue).to(fanoutExchange);
//        amqpAdmin.declareBinding(binding);
        if(msg instanceof String){
            MessageProperties messageProperties = new MessageProperties();
            //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            Message message = new Message(((String) msg).getBytes(),messageProperties);
            rabbitTemplate.convertAndSend("exchange",queue.getName(),message);
        }else if(msg instanceof ChannelBean){
            rabbitTemplate.convertAndSend("exchange",queue.getName(), msg);
        }
    }



    /**
     * 删除一个queue
     * @param queueName queueName
     */
    public boolean deleteQueue(String queueName){
        return amqpAdmin.deleteQueue(queueName);
    }


}
