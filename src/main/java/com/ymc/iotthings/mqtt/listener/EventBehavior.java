package com.ymc.iotthings.mqtt.listener;

/**
 * @author ymc
 * @date 2019年7月5日 11:26:57
 */
public enum EventBehavior {
    /**
     * 继续消息传递
     */
    CONTINUE,
    /**
     * 停止消息传递
     */
    BREAK;
}
