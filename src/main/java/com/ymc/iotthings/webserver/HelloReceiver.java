package com.ymc.iotthings.webserver;

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
@RabbitListener(queues = "hello") //监听 名称为 hello 的queue
//@RabbitListener(queues = "hello", containerFactory="rabbitListenerContainerFactory")
public class HelloReceiver {
    @Resource
    private AmqpTemplate amqpTemplate;

    //消息处理器
    @RabbitHandler
    public void process(String message){
        System.out.println("mq接收到数据 hello Receiver:"+message);
        // 转发给 hello2
        amqpTemplate.convertAndSend("Hello2",message);
    }

}
