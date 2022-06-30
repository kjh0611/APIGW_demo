package com.mobigen.ni.gatewayservicekjh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GatewayServiceKjhApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceKjhApplication.class, args);
	}

}
