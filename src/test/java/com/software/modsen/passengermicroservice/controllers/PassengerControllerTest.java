package com.software.modsen.passengermicroservice.controllers;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import com.software.modsen.passengermicroservice.entities.PassengerPatchDto;
import com.software.modsen.passengermicroservice.mappers.PassengerMapper;
import com.software.modsen.passengermicroservice.services.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {
    @Mock
    PassengerService passengerService;

    @Mock
    PassengerMapper passengerMapper;

    @InjectMocks
    PassengerController passengerController;

    @BeforeEach
    void setUp() {
        passengerMapper = PassengerMapper.INSTANCE;
    }

    private List<Passenger> initPassengers() {
        return List.of(
                new Passenger(1, "Alex", "alex@gmail.com",
                        "+375299999999", false),
                new Passenger(2, "Ivan", "ivan@gmail.com",
                        "+375332929293", false));
    }

    @Test
    @DisplayName("Getting all passengers.")
    void getAllPassengersTest_ReturnsValidResponseEntity() {
        //given
        List<Passenger> passengersFromDb = initPassengers();
        doReturn(passengersFromDb).when(this.passengerService).getAllPassengers(true);

        //when
        ResponseEntity<List<Passenger>> responseEntity = passengerController.getAllPassengers(true);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(passengersFromDb, responseEntity.getBody());
    }

    @Test
    @DisplayName("Getting all not deleted passengers.")
    void getAllNotDeletedPassengersTest_ReturnsValidResponseEntity() {
        //given
        List<Passenger> passengersFromDb = initPassengers();
        doReturn(passengersFromDb).when(this.passengerService).getAllPassengers(false);

        //when
        ResponseEntity<List<Passenger>> responseEntity = passengerController.getAllPassengers(false);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(passengersFromDb, responseEntity.getBody());
    }

    @Test
    @DisplayName("Getting passenger by id.")
    void getPassengerTest_ReturnsValidResponseEntity() {
        //given
        Passenger passengerFromDb = new Passenger(1, "Alex", "post@gmail.com",
                "+37441234567", false);
        doReturn(passengerFromDb).when(this.passengerService).getPassengerById(1);

        //when
        ResponseEntity<Passenger> responseEntity = passengerController.getPassenger(1);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(passengerFromDb, responseEntity.getBody());
    }

    @Test
    @DisplayName("Save new passenger.")
    void savePassengerTest_ReturnsValidResponseEntity() {
        //given
        PassengerDto passengerDto = new PassengerDto("Vova", "vova@gmail.com",
                "+375251100333");
        Passenger savedPassenger = new Passenger(1, "Vova", "vova@gmail.com",
                "+375251100333", false);
        doReturn(savedPassenger).when(this.passengerService).savePassenger(
                passengerMapper.fromPassengerDtoToPassenger(passengerDto)
        );

        //when
        ResponseEntity<Passenger> responseEntity = passengerController.savePassenger(passengerDto);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(Passenger.class, responseEntity.getBody());
        assertNotEquals(responseEntity.getBody().getId(), 0);
        assertEquals(savedPassenger.getName(), responseEntity.getBody().getName());
        assertEquals(savedPassenger.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(savedPassenger.getPhoneNumber(), responseEntity.getBody().getPhoneNumber());
        assertFalse(responseEntity.getBody().isDeleted());
        verify(this.passengerService).savePassenger(
                passengerMapper.fromPassengerDtoToPassenger(passengerDto));
    }

    @Test
    @DisplayName("Update passenger by id.")
    void updatePassengerTest_ReturnsValidResponseEntity() {
        //given
        int passengerId = 1;
        PassengerDto passengerDto = new PassengerDto("Vovchik", "vovchik@gmail.com",
                "+375251100333");
        Passenger updatingPassenger = new Passenger(passengerId, "Vovchik", "vovchik@gmail.com",
                "+375251111333", false);
        when(this.passengerService.updatePassengerById(passengerId,
                        passengerMapper.fromPassengerDtoToPassenger(passengerDto)))
                .thenReturn(updatingPassenger);

        //when
        ResponseEntity<Passenger> responseEntity = passengerController.updatePassenger(passengerId, passengerDto);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(Passenger.class, responseEntity.getBody());
        assertNotEquals(responseEntity.getBody().getId(), 0);
        assertEquals(updatingPassenger.getName(), responseEntity.getBody().getName());
        assertEquals(updatingPassenger.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(updatingPassenger.getPhoneNumber(), responseEntity.getBody().getPhoneNumber());
        assertFalse(responseEntity.getBody().isDeleted());
        verify(this.passengerService).updatePassengerById(passengerId,
                passengerMapper.fromPassengerDtoToPassenger(passengerDto));
    }

    @Test
    @DisplayName("Partially update passenger by id.")
    void patchPassengerTest_ReturnsValidResponseEntity() {
        //given
        int passengerId = 1;
        PassengerPatchDto passengerPatchDto = new PassengerPatchDto("CrazyVovchik", null,
                null);
        Passenger updatingPassenger = new Passenger(passengerId, "CrazyVovchik", "vovchik@gmail.com",
                "+375251111333", false);
        when(this.passengerService.patchPassengerById(passengerId,
                        passengerMapper.fromPassengerPatchDtoToPassengerRating(passengerPatchDto)))
                .thenReturn(updatingPassenger);

        //when
        ResponseEntity<Passenger> responseEntity = passengerController.patchPassenger(passengerId, passengerPatchDto);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(Passenger.class, responseEntity.getBody());
        assertNotEquals(responseEntity.getBody().getId(), 0);
        assertEquals(updatingPassenger.getName(), responseEntity.getBody().getName());
        assertEquals(updatingPassenger.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(updatingPassenger.getPhoneNumber(), responseEntity.getBody().getPhoneNumber());
        assertFalse(responseEntity.getBody().isDeleted());
        verify(this.passengerService).patchPassengerById(passengerId,
                passengerMapper.fromPassengerPatchDtoToPassengerRating(passengerPatchDto));
    }

    @Test
    @DisplayName("Soft delete passenger by id.")
    void softDeletePassengerTest_ReturnsValidResponseEntity() {
        //given
        int passengerId = 1;
        Passenger softDeletedPassenger = new Passenger(passengerId, "Igor", "igor@gmail.com",
                "+375255555333", true);
        doReturn(softDeletedPassenger).when(this.passengerService).softDeletePassengerById(passengerId);

        //when
        ResponseEntity<Passenger> responseEntity = passengerController.softDeletePassenger(passengerId);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(Passenger.class, responseEntity.getBody());
        assertNotEquals(responseEntity.getBody().getId(), 0);
        assertEquals(softDeletedPassenger.getName(), responseEntity.getBody().getName());
        assertEquals(softDeletedPassenger.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(softDeletedPassenger.getPhoneNumber(), responseEntity.getBody().getPhoneNumber());
        assertTrue(responseEntity.getBody().isDeleted());
    }

    @Test
    @DisplayName("Soft recovery passenger by id.")
    void softRecoveryPassengerTest_ReturnsValidResponseEntity() {
        //given
        int passengerId = 1;
        Passenger softRecoveryPassenger = new Passenger(passengerId, "Igor", "igor@gmail.com",
                "+375255555333", false);
        doReturn(softRecoveryPassenger).when(this.passengerService).softRecoveryPassengerById(passengerId);

        //when
        ResponseEntity<Passenger> responseEntity = passengerController.softRecoveryPassenger(passengerId);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(Passenger.class, responseEntity.getBody());
        assertNotEquals(responseEntity.getBody().getId(), 0);
        assertEquals(softRecoveryPassenger.getName(), responseEntity.getBody().getName());
        assertEquals(softRecoveryPassenger.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(softRecoveryPassenger.getPhoneNumber(), responseEntity.getBody().getPhoneNumber());
        assertFalse(responseEntity.getBody().isDeleted());
    }
}