package com.ymc.iotthings;

import com.ymc.iotthings.webserver.beanutils.Init;
import com.ymc.iotthings.webserver.WebSocketServer;
import com.ymc.iotthings.webserver.sendserver.WebSendServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

@SpringBootApplication
@ComponentScan("com.ymc")
public class IotthingsApplication implements CommandLineRunner {

	@Autowired
	WebSocketServer webSocketServer;
	@Resource
	WebSendServer webSendServer;
	public static void main(String[] args) {
		SpringApplication.run(IotthingsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		webSocketServer.run(Init.PORT);
		webSendServer.run(Init.SEND_PORT);
	}
}
