package com.software.modsen.passengermicroservice.integration;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.repositories.PassengerAccountRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import com.software.modsen.passengermicroservice.services.PassengerRatingService;
import com.software.modsen.passengermicroservice.services.PassengerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PassengerRatingControllerIntegrationTest extends TestconteinersConfig {
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
    void getAllPassengerRatingsTest_ReturnsPassengerRatings() {
        //given
        List<Passenger> passengers = defaultPassengers();
        for (Passenger passenger : passengers) {
            passengerService.savePassenger(passenger);
        }

        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/rating")
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
                () -> assertTrue(responseContent.contains("0"))
        );
    }

    @Test
    @SneakyThrows
    void getAllNotDeletedPassengerRatingsTest_ReturnsValidPassengerRatings() {
        //given
        List<Passenger> passengers = defaultPassengers();
        for (Passenger passenger : passengers) {
            passengerService.savePassenger(passenger);
        }

        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/rating/not-deleted")
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
                () -> assertTrue(responseContent.contains("0"))
        );
    }

    @Test
    @SneakyThrows
    void getPassengerRatingByIdTest_ReturnsPassengerRating() {
        //given
        Passenger passenger = defaultPassengers().get(0);
        passenger = passengerService.savePassenger(passenger);
        PassengerRating passengerRating = passengerRatingRepository.findByPassengerId(passenger.getId()).get();

        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/rating/" + passengerRating.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("0.0")),
                () -> assertTrue(responseContent.contains("0"))
        );
    }

    @Test
    @SneakyThrows
    void getPassengerRatingByPassengerIdTest_ReturnsPassengerRating() {
        //given
        Passenger passenger = defaultPassengers().get(1);
        passenger = passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/rating/" + passenger.getId() +
                        "/by-passenger")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ignat")),
                () -> assertTrue(responseContent.contains("0.0")),
                () -> assertTrue(responseContent.contains("0"))
        );
    }

    private final String passengerRatingDto = """
                {
                    "rating_value": 5,
                    "number_of_ratings": 100
                }
            """;

    @Test
    @SneakyThrows
    void updatePassengerRatingTest_ReturnsPassengerRating() {
        //given
        Passenger passenger = defaultPassengers().get(0);
        passenger = passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(put("/api/passenger/rating/" + passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerRatingDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("5")),
                () -> assertTrue(responseContent.contains("100"))
        );
    }

    @Test
    @SneakyThrows
    void patchPassengerRatingByIdTest_ReturnsPassengerRating() {
        //given
        Passenger passenger = defaultPassengers().get(0);
        passenger = passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(patch("/api/passenger/rating/" + passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerRatingDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("5")),
                () -> assertTrue(responseContent.contains("100"))
        );
    }
}