package com.ymc.iotthings.webserver.rabbitmq.customize;

import org.springframework.amqp.core.*;
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

    /**
     * 创建人：张博
     * 时间：2018/3/5 上午10:45
     * @apiNote 定义扇出（广播）交换器
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout-exchange");
    }

    /**
     * 创建人：张博
     * 时间：2018/3/5 上午10:48
     * @apiNote 定义自动删除匿名队列
     */
    @Bean
    public Queue autoDeleteQueue() {
        return new AnonymousQueue();
    }

    /**
     * 创建人：张博
     * 时间：2018/3/5 上午10:48
     * @param fanoutExchange 扇出（广播）交换器
     * @param autoDeleteQueue0 自动删除队列
     * @apiNote 把队列绑定到扇出（广播）交换器
     * @return Binding
     */
    @Bean
    public Binding binding(FanoutExchange fanoutExchange, Queue autoDeleteQueue0) {
        return BindingBuilder.bind(autoDeleteQueue0).to(fanoutExchange);
    }

}
