package com.software.modsen.passengermicroservice.integration;

import com.software.modsen.passengermicroservice.entities.Passenger;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PassengerControllerIntegrationTest {
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

    static boolean isAlreadySetUped = false;

    @BeforeEach
    void setUp() {
        if (!isAlreadySetUped) {
            List<Passenger> passengers = defaultPassengers();
            for (Passenger passenger : passengers) {
                passengerService.savePassenger(passenger);
            }
            isAlreadySetUped = true;
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
    @Order(1)
    @SneakyThrows
    void getAllPassengersTest_ReturnsPassengers() {
        //given
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
    @Order(2)
    @SneakyThrows
    void getAllNotDeletedPassengersTest_ReturnsValidPassengers() {
        //given
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
    @Order(3)
    @SneakyThrows
    void getPassengerTest_ReturnsPassenger() {
        //given
        MvcResult mvcResult = mockMvc.perform(get("/api/passenger/1")
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
    @Order(4)
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
                () -> assertTrue(responseContent.contains("5")),
                () -> assertTrue(responseContent.contains("Vova")),
                () -> assertTrue(responseContent.contains("vova@gmail.com")),
                () -> assertTrue(responseContent.contains("+375443377999")),
                () -> assertTrue(responseContent.contains("false"))
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
    @Order(5)
    @SneakyThrows
    void updatePassengerTest_ReturnsPassenger() {
        //given
        MvcResult mvcResult = mockMvc.perform(put("/api/passenger/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerUpdateDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("2")),
                () -> assertTrue(responseContent.contains("Andrei")),
                () -> assertTrue(responseContent.contains("andrei@gmail.com")),
                () -> assertTrue(responseContent.contains("+375293344555")),
                () -> assertTrue(responseContent.contains("false"))
        );
    }

    private final String passengerPatchDto = """
                {
                    "name": "Vasya",
                    "email": "vasya@gmail.com"
                }
            """;

    @Test
    @Order(6)
    @SneakyThrows
    void patchPassengerTest_ReturnsPassenger() {
        //given
        MvcResult mvcResult = mockMvc.perform(patch("/api/passenger/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerPatchDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("1")),
                () -> assertTrue(responseContent.contains("Vasya")),
                () -> assertTrue(responseContent.contains("vasya@gmail.com")),
                () -> assertTrue(responseContent.contains("+375293333333")),
                () -> assertTrue(responseContent.contains("false"))
        );
    }

    @Test
    @Order(7)
    @SneakyThrows
    void softDeletePassengerTest_ReturnsPassenger() {
        //given
        MvcResult mvcResult = mockMvc.perform(post("/api/passenger/4/soft-delete")
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
    @Order(8)
    @SneakyThrows
    void softRecoveryPassengerTest_ReturnsPassenger() {
        //given
        MvcResult mvcResult = mockMvc.perform(post("/api/passenger/3/soft-recovery")
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