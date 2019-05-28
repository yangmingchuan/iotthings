package com.ymc.iotthings.webserver.beanutils;

/**
 * 常量
 *
 * package name: com.vip.things.netty.utils
 * date :2019/3/27
 * author : ymc
 **/

public class Init {

    public static int PORT = 11111;
    static String HOST = "127.0.0.1";
    public static String WEB_SOCKET_URL = String.format("ws://%s:%d/websocket", HOST, PORT);

    public static int SEND_PORT = 22222;
    static String SEND_HOST = "127.0.0.1";
    public static String SEND_WEB_SOCKET_URL = String.format("ws://%s:%d/websocket", HOST, PORT);

    public static final int SERVER_READ_IDEL_TIME_OUT = 10;
    public static final int SERVER_WRITE_IDEL_TIME_OUT = 0;
    public static final int SERVER_ALL_IDEL_TIME_OUT = 0;
}
