package com.ymc.iotthings.webserver.rabbitmq;

import org.springframework.stereotype.Component;

/**
 * 固定订阅某个Queue,当同时订阅时，因为不是广播，所以会随机消费
 * @author lc
 */
@Component
public class RabbitReceive {

//    @RabbitHandler
//    @RabbitListener(queues = "#{autoDeleteQueue.name}")
//    public void processMessage(ChannelBean content){
//            System.out.println("receiver bean :" + content.getLineId());
//    }
}
