package com.ymc.iotthings.mqtt.server;

import io.netty.channel.ChannelHandlerContext;

/**
 * package name: com.yb.socket.status
 * date :2019/7/2
 * author : ymc
 **/
public interface MqttTransferListener {

    void transMessage(ChannelHandlerContext ctx, Object msg);

}
