package com.ymc.iotthings.webserver.rabbitmq.customize;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * rabbitmq 配置
 *
 * package name: com.ymc.iotthings.webserver.rabbitmq.customize
 * date :2019/5/14
 * author : ymc
 **/

@Component
public class RabbitmqConfig {

    private AmqpAdmin amqpAdmin;
    private String queueName = "hello.#";

    @Autowired
    public void setAmqpAdmin(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    /**
     *  项目启动即能创建的Exchange
     *  可以创建各种类型的Exchange，父类都是 AbstractExchange
     *  这里举例Topic类型
     *  如果需要创建多个同类型可以用@Bean(name="beanName")，引用时用@Qualifier("beanName" )
     */
    @Bean
    public TopicExchange exchange(){
        String exchangeName = "exchange";
        TopicExchange dataExchange = new TopicExchange(exchangeName,true,false);
        amqpAdmin.declareExchange(dataExchange);
        return dataExchange;
    }

    /**
     * 项目创建就生成的Queue
     * @return
     */
    @Bean
    public Queue queue(){
        Queue queue = new Queue(queueName,true,false,false);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with(queueName);
    }


}
