package com.ymc.iotthings.webserver;

import com.ymc.iotthings.webserver.sendserver.WebSendServer;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * package name: com.vip.things.rabbit
 * date :2019/3/26
 * author : ymc
 **/

@Component
@RabbitListener(queues = "Hello2")
public class Receiver2 {

    @Resource
    WebSendServer webSendServer;

    private boolean hasFisrt;

    @RabbitHandler
    public void receiver(String msg) throws Exception {
        System.out.println("mq hello2 receiver2:"+msg);
        if(!hasFisrt){
            hasFisrt = true;
            webSendServer.run(Init.SEND_PORT);
        }
    }

}
