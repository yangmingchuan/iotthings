package com.ymc.iotthings.webserver.beanutils;

import io.netty.channel.ChannelId;

import java.io.Serializable;

/**
 * package name: com.ymc.iotthings.webserver
 * date :2019/5/8
 * author : ymc
 **/

public class ChannelBean implements Serializable{

    /**
     * 分组id
     */
    private int lineId;
    /**
     * 设备id
     */
    private ChannelId channelId;

    /**
     * 连接标识
     */
    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ChannelId getChannelId() {
        return channelId;
    }

    public void setChannelId(ChannelId channelId) {
        this.channelId = channelId;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }
}
