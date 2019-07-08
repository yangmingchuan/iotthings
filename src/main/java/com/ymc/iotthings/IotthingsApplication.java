package com.ymc.iotthings;

import com.ymc.iotthings.mqtt.MqttClientTest1;
import com.ymc.iotthings.mqtt.MqttServerTest;
import com.ymc.iotthings.webserver.WebSocketServer;
import com.ymc.iotthings.webserver.beanutils.Init;
import com.ymc.iotthings.webserver.sendserver.WebSendServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@ComponentScan("com.ymc")
public class IotthingsApplication implements CommandLineRunner {

	@Autowired
	WebSocketServer webSocketServer;
	@Resource
	WebSendServer webSendServer;

	@Autowired
	MqttServerTest mqttServerTest;

	@Autowired
	MqttClientTest1 mqttClientTest1;

	/**
	 * 定长 线程池
	 */
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

	public static void main(String[] args) {
		SpringApplication.run(IotthingsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		fixedThreadPool.execute(() -> {
//            try {
//                webSendServer.run(Init.SEND_PORT);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
		fixedThreadPool.execute(() -> {
			try {
				webSocketServer.run(Init.PORT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		mqttServerTest.run();
		mqttClientTest1.run();
	}
}
