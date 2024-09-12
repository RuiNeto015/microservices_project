package com.isep.acme;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import java.awt.image.BufferedImage;
import java.util.UUID;

@SpringBootApplication()
public class ACMEApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(ACMEApplication.class, args);
	}

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}

	@Bean
	public UUID microserviceInstanceId() {
		return UUID.randomUUID();
	}
}
