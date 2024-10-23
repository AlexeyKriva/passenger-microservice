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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PassengerControllerIntegrationTest extends TestconteinersConfig {
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
                        .build(),
                Passenger.builder()
                        .name("Kolya")
                        .email("kolya@gmail.com")
                        .phoneNumber("+375292342341")
                        .isDeleted(false)
                        .build()
        );
    }

    @Test
    @SneakyThrows
    void getAllPassengersTest_ReturnsPassengers() {
        //given
        List<Passenger> passengers = defaultPassengers();
        for (Passenger passenger : passengers) {
            passengerService.savePassenger(passenger);
        }

        MvcResult mvcResult = mockMvc.perform(get("/api/passenger")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println(responseContent);

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("igor@gmail.com")),
                () -> assertTrue(responseContent.contains("+375293333333")),
                () -> assertTrue(responseContent.contains("Ignat")),
                () -> assertTrue(responseContent.contains("ignat@gmail.com")),
                () -> assertTrue(responseContent.contains("+375297777777")),
                () -> assertTrue(responseContent.contains("Maksim")),
                () -> assertTrue(responseContent.contains("maksim@gmail.com")),
                () -> assertTrue(responseContent.contains("+375291111111")),
                () -> assertTrue(responseContent.contains("Kolya")),
                () -> assertTrue(responseContent.contains("kolya@gmail.com")),
                () -> assertTrue(responseContent.contains("+375292342341"))
        );
    }

    @Test
    @SneakyThrows
    void getAllNotDeletedPassengersTest_ReturnsValidPassengers() {
        //given
        List<Passenger> passengers = defaultPassengers();
        for (Passenger passenger : passengers) {
            passengerService.savePassenger(passenger);
        }

        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/not-deleted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("igor@gmail.com")),
                () -> assertTrue(responseContent.contains("+375293333333")),
                () -> assertTrue(responseContent.contains("Ignat")),
                () -> assertTrue(responseContent.contains("ignat@gmail.com")),
                () -> assertTrue(responseContent.contains("+375297777777")),
                () -> assertFalse(responseContent.contains("Maksim")),
                () -> assertFalse(responseContent.contains("maksim@gmail.com")),
                () -> assertFalse(responseContent.contains("+375291111111")),
                () -> assertTrue(responseContent.contains("Kolya")),
                () -> assertTrue(responseContent.contains("kolya@gmail.com")),
                () -> assertTrue(responseContent.contains("+375292342341"))
        );
    }

    @Test
    @SneakyThrows
    void getPassengerTest_ReturnsPassenger() {
        //given
        Passenger passenger = defaultPassengers().get(0);
        passenger = passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/" + passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Igor")),
                () -> assertTrue(responseContent.contains("igor@gmail.com")),
                () -> assertTrue(responseContent.contains("+375293333333"))
        );
    }

    private final String passengerDto = """
                {
                    "name": "Vova",
                    "email": "vova@gmail.com",
                    "phone_number": "+375443377999"
                }
            """;

    @Test
    @SneakyThrows
    void savePassengerTest_ReturnsPassenger() {
        //given
        MvcResult mvcResult = mockMvc.perform(post("/api/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Vova")),
                () -> assertTrue(responseContent.contains("vova@gmail.com")),
                () -> assertTrue(responseContent.contains("+375443377999"))
        );
    }

    private final String passengerUpdateDto = """
                {
                    "name": "Andrei",
                    "email": "andrei@gmail.com",
                    "phone_number": "+375293344555"
                }
            """;

    @Test
    @SneakyThrows
    void updatePassengerTest_ReturnsPassenger() {
        //given
        Passenger passenger = defaultPassengers().get(1);
        passenger = passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(put("/api/passenger/" + passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerUpdateDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Andrei")),
                () -> assertTrue(responseContent.contains("andrei@gmail.com")),
                () -> assertTrue(responseContent.contains("+375293344555"))
        );
    }

    private final String passengerPatchDto = """
                {
                    "name": "Vasya",
                    "email": "vasya@gmail.com"
                }
            """;

    @Test
    @SneakyThrows
    void patchPassengerTest_ReturnsPassenger() {
        //given
        Passenger passenger = defaultPassengers().get(3);
        passenger = passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(patch("/api/passenger/" + passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerPatchDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Vasya")),
                () -> assertTrue(responseContent.contains("vasya@gmail.com")),
                () -> assertTrue(responseContent.contains("+375292342341"))
        );
    }

    @Test
    @SneakyThrows
    void softDeletePassengerTest_ReturnsPassenger() {
        //given
        Passenger passenger = defaultPassengers().get(3);
        passenger = passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(post("/api/passenger/" + passenger.getId() + "/soft-delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("4")),
                () -> assertTrue(responseContent.contains("Kolya")),
                () -> assertTrue(responseContent.contains("kolya@gmail.com")),
                () -> assertTrue(responseContent.contains("+375292342341")),
                () -> assertTrue(responseContent.contains("true"))
        );
    }

    @Test
    @SneakyThrows
    void softRecoveryPassengerTest_ReturnsPassenger() {
        //given
        Passenger passenger = defaultPassengers().get(2);
        passenger = passengerService.savePassenger(passenger);

        MvcResult mvcResult = mockMvc.perform(post("/api/passenger/" + passenger.getId() + "/soft-recovery")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("3")),
                () -> assertTrue(responseContent.contains("Maksim")),
                () -> assertTrue(responseContent.contains("maksim@gmail.com")),
                () -> assertTrue(responseContent.contains("+375291111111")),
                () -> assertTrue(responseContent.contains("false"))
        );
    }
}