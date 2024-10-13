package com.software.modsen.passengermicroservice;

import com.software.modsen.passengermicroservice.controllers.PassengerAccountController;
import com.software.modsen.passengermicroservice.controllers.PassengerController;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.wait.strategy.Wait.forListeningPort;

@AutoConfigureMessageVerifier
@Testcontainers
@SpringBootTest(classes = PassengerMicroserviceApplication.class)
public class BaseTestClass {
    @Autowired
    private PassengerController passengerController;

    @Autowired
    private PassengerAccountController passengerAccountController;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15"))
            .withDatabaseName("cab-aggregator-db")
            .withUsername("postgres")
            .withPassword("98479847")
            .waitingFor(forListeningPort());

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    static int id = 1;

    @BeforeEach
    void setup() {
        passengerController.savePassenger(new PassengerDto("Ivan" + id, "ivan" + id + "@gmail.com",
                "+37529123987" + id++));

        RestAssuredMockMvc.standaloneSetup(passengerController, passengerAccountController);
    }
}