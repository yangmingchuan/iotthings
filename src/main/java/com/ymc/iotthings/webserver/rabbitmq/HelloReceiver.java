package com.ymc.iotthings.webserver.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *  hello 接收端
 *
 * package name: com.vip.things.rabbit
 * date :2019/3/25
 * author : ymc
 **/

@Component
public class HelloReceiver {
    @Resource
    private AmqpTemplate amqpTemplate;

    //消息处理器
    @RabbitHandler
    @RabbitListener(queues = "hello.", containerFactory="rabbitListenerContainerFactory")
    public void process(String message){
        System.out.println("mq接收到数据 hello Receiver:"+message);
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(new Jackson2JsonMessageConverter());
//        return template;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        return factory;
//    }

}
