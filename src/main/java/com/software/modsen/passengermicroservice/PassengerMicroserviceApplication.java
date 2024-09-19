package com.software.modsen.passengermicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class PassengerMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PassengerMicroserviceApplication.class, args);
    }
}
