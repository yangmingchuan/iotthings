package com.ymc.iotthings.webserver;

import io.netty.channel.Channel;

/**
 * package name: com.ymc.iotthings.webserver
 * date :2019/5/8
 * author : ymc
 **/

public class ChannelBean {

    private Channel channel;

    private int lineId;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }
}
