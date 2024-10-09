package com.software.modsen.passengermicroservice.integration;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.services.PassengerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
public class PassengerAccountControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerService passengerService;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15"))
            .withDatabaseName("cab-aggregator-db")
            .withUsername("postgres")
            .withPassword("98479847");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        List<Passenger> passengers = defaultPassengers();
        for (Passenger passenger: passengers) {
            passengerService.savePassenger(passenger);
        }
    }

    private static List<Passenger> defaultPassengers() {
        return List.of(
                Passenger.builder()
                        .name("Igor")
                        .email("igor@gmail.com")
                        .phoneNumber("+375293333333")
                        .isDeleted(false)
                        .build(),
                Passenger.builder()
                        .name("Ignat")
                        .email("ignat@gmail.com")
                        .phoneNumber("+375297777777")
                        .isDeleted(false)
                        .build(),
                Passenger.builder()
                        .name("Maksim")
                        .email("maksim@gmail.com")
                        .phoneNumber("+375291111111")
                        .isDeleted(true)
                        .build()
        );
    }

    @Test
    @SneakyThrows
    void getAllPassengerAccountsTest_ReturnsPassengerAccounts() {
        //given
        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("Ignat")),
                () -> assertTrue(responseContent.contains("Maksim")),
                () -> assertTrue(responseContent.contains("0.0")),
                () -> assertTrue(responseContent.contains("BYN"))
        );
    }

    @Test
    @SneakyThrows
    void getAllNotDeletedPassengerAccounts_ReturnsPassengerAccounts() {
        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/account/not-deleted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("Ignat")),
                () -> assertFalse(responseContent.contains("Maksim")),
                () -> assertTrue(responseContent.contains("0.0")),
                () -> assertTrue(responseContent.contains("BYN"))
        );
    }

    @Test
    @SneakyThrows
    void getNotDeletedPassengerAccountsByIdTest_ReturnsPassengerAccount() {
        //given
        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/account/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("0.0")),
                () -> assertTrue(responseContent.contains("BYN"))
        );
    }

    @Test
    @SneakyThrows
    void getNotDeletedPassengerAccountsByPassengerIdTest_ReturnsPassengerAccount (){
        //given
        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/account/2/by-passenger")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ignat")),
                () -> assertTrue(responseContent.contains("0.0")),
                () -> assertTrue(responseContent.contains("BYN"))
        );
    }

    private final String passengerAccountIncreaseDto = """
            {
                "balance": 1000.0,
                "currency": "BYN"
            }
            """;

    private final String passengerAccountCancelDto = """
            {
                "balance": 800.0,
                "currency": "BYN"
            }
            """;

    @Test
    @SneakyThrows
    void increaseBalanceByPassengerIdTest_ReturnsPassengerAccount() {
        //given
        MvcResult mvcResult = mockMvc.perform(put("/api/passenger/account/1/increase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerAccountIncreaseDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("1000.0")),
                () -> assertTrue(responseContent.contains("BYN"))
        );
    }

    @Test
    @SneakyThrows
    void cancelBalanceByPassengerIdTest_ReturnsPassengerAccount() {
        //given
        mockMvc.perform(put("/api/passenger/account/1/increase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passengerAccountIncreaseDto));

        MvcResult mvcResult = mockMvc.perform(put("/api/passenger/account/1/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerAccountCancelDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("200.0")),
                () -> assertTrue(responseContent.contains("BYN"))
        );
    }
}
