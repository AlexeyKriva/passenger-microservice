package com.software.modsen.passengermicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableDiscoveryClient
public class PassengerMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PassengerMicroserviceApplication.class, args);
    }
}
