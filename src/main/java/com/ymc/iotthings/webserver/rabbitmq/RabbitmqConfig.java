package com.ymc.iotthings.webserver.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * rabbitmq 配置
 * <p>
 * package name: com.ymc.iotthings.webserver.rabbitmq.customize
 * date :2019/5/14
 * author : ymc
 **/

@Component
public class RabbitmqConfig {

    private final static String message = "web.socket.message";
    private final static String messages = "send.socket.message";

    @Bean
    public Queue queueMessage() {
        return new Queue(RabbitmqConfig.message);
    }

    @Bean
    public Queue queueMessages() {
        return new Queue(RabbitmqConfig.messages);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("web.#");
    }

    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("send.#");
    }

}
