package com.software.modsen.passengermicroservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRetry
@EnableTransactionManagement
@EnableMongoRepositories
@EnableWebFlux
@OpenAPIDefinition(
        info = @Info(
                title = "Passenger API",
                description = "Passenger microservice for Modsen internship",
                contact = @Contact(
                        name = "Alexey Kryvetski",
                        email = "alexey.kriva03.com@gmail.com"
                )
        )
)
public class PassengerMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PassengerMicroserviceApplication.class, args);
    }
}
