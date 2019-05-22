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
    private final FanoutExchange fanoutExchange;
    private final RabbitmqConfig rabbitmqConfig;
    @Autowired
    public MQSender(AmqpAdmin amqpAdmin, RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange,RabbitmqConfig rabbitmqConfig) {
        LOGGER.info("MQSender 初始化中...");
        this.amqpAdmin = amqpAdmin;
        this.rabbitTemplate = rabbitTemplate;
        this.fanoutExchange = fanoutExchange;
        this.rabbitmqConfig = rabbitmqConfig;
    }
    /**
     * 给queue发送消息
     * @param queueName queueName
     * @param msg msg
     */
    public <T> void send(String queueName,T msg){
        // 动态创建
        amqpAdmin.declareExchange(new FanoutExchange("amqpadmin.exchange"));
        amqpAdmin.declareQueue(new Queue("amqpadmin.queue",true));
//        Queue queue = rabbitmqConfig.autoDeleteQueue();
//        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(new Binding("amqpadmin.queue",
                Binding.DestinationType.QUEUE,"amqpadmin.exchange","amqp.haha",null));
//        Binding binding = BindingBuilder.bind(queue).to(fanoutExchange);
//        amqpAdmin.declareBinding(binding);
        if(msg instanceof String){
            MessageProperties messageProperties = new MessageProperties();
            //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            Message message = new Message(((String) msg).getBytes(),messageProperties);
            rabbitTemplate.convertAndSend("amqpadmin.exchange","amqpadmin.queue",message);
        }else if(msg instanceof ChannelBean){
            rabbitTemplate.convertAndSend("amqpadmin.exchange","amqpadmin.queue", msg);
        }
    }

    /**
     * 给queue发送消息
     * @param queueName queueName
     * @param msg msg
     */
    public <T> void webSend(String queueName,T msg){
        Queue queue = rabbitmqConfig.autoWebDeleteQueue();
        amqpAdmin.declareQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(fanoutExchange);
        amqpAdmin.declareBinding(binding);
        if(msg instanceof String){
            MessageProperties messageProperties = new MessageProperties();
            //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            Message message = new Message(((String) msg).getBytes(),messageProperties);
            rabbitTemplate.convertAndSend("fanout-web-exchange",queueName,message);
        }else if(msg instanceof ChannelBean){
            rabbitTemplate.convertAndSend("fanout-web-exchange",queueName, msg);
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
