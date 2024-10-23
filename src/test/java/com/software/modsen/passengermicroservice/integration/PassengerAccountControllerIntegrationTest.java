package com.software.modsen.passengermicroservice.integration;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.repositories.PassengerAccountRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import com.software.modsen.passengermicroservice.services.PassengerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PassengerAccountControllerIntegrationTest extends TestconteinersConfig {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerRatingRepository passengerRatingRepository;

    @Autowired
    private PassengerAccountRepository passengerAccountRepository;

    @AfterEach
    void setDown() {
        passengerAccountRepository.deleteAll();
        passengerRatingRepository.deleteAll();
        passengerRepository.deleteAll();
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
        List<Passenger> passengers = defaultPassengers();
        for (Passenger passenger : passengers) {
            passengerService.savePassenger(passenger);
        }

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
        //given
        List<Passenger> passengers = defaultPassengers();
        for (Passenger passenger : passengers) {
            passengerService.savePassenger(passenger);
        }

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
        Passenger passenger = defaultPassengers().get(0);
        passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/account/" + passenger.getId())
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
    void getNotDeletedPassengerAccountsByPassengerIdTest_ReturnsPassengerAccount() {
        //given
        Passenger passenger = defaultPassengers().get(1);
        passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/account/" + passenger.getId()
                        + "/by-passenger")
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

    private final String passengerAccountUpDto = """
            {
                "balance": 1000.0,
                "currency": "BYN"
            }
            """;

    private final String passengerAccountDownDto = """
            {
                "balance": 800.0,
                "currency": "BYN"
            }
            """;

    @Test
    @SneakyThrows
    void increaseBalanceByPassengerIdTest_ReturnsPassengerAccount() {
        //given
        Passenger passenger = defaultPassengers().get(0);
        passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(put("/api/passenger/account/" + passenger.getId() +
                        "/increase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerAccountUpDto))
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
        Passenger passenger = defaultPassengers().get(1);
        passengerService.savePassenger(passenger);

        mockMvc.perform(put("/api/passenger/account/" + passenger.getId() +
                "/increase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passengerAccountUpDto));

        MvcResult mvcResult = mockMvc.perform(put("/api/passenger/account/" + passenger.getId() +
                        "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerAccountDownDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ignat")),
                () -> assertTrue(responseContent.contains("200.0")),
                () -> assertTrue(responseContent.contains("BYN"))
        );
    }
}