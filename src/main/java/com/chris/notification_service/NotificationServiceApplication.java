package com.chris.notification_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.chris"})
@OpenAPIDefinition(info = @Info(title = "REST API FOR NOTIFICATION SERVICE", version = "0.1.0", description = "NOTIFICATION SERVICE API DOCUMENTATION"))
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
