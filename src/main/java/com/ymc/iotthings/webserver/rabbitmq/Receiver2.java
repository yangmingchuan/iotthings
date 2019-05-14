package com.ymc.iotthings.webserver.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * package name: com.vip.things.rabbit
 * date :2019/3/26
 * author : ymc
 **/

@Component
public class Receiver2 {

    @RabbitHandler
    @RabbitListener(queues = "hello",containerFactory="rabbitListenerContainerFactory")
    public void receiver(String msg) throws Exception {
        System.out.println("mq hello2 receiver2:"+msg);
    }

}
