package com.ymc.iotthings;

import com.ymc.iotthings.webserver.Init;
import com.ymc.iotthings.webserver.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ymc")
public class IotthingsApplication implements CommandLineRunner {

	@Autowired
	WebSocketServer webSocketServer;

	public static void main(String[] args) {
		SpringApplication.run(IotthingsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		webSocketServer.run(Init.PORT);
	}
}
