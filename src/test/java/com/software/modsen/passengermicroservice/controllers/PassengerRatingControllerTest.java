//package com.software.modsen.passengermicroservice.controllers;
//
//import com.software.modsen.passengermicroservice.entities.Passenger;
//import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
//import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPatchDto;
//import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPutDto;
//import com.software.modsen.passengermicroservice.mappers.PassengerRatingMapper;
//import com.software.modsen.passengermicroservice.services.PassengerRatingService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//public class PassengerRatingControllerTest {
//    @Mock
//    PassengerRatingService passengerRatingService;
//
//    @Mock
//    PassengerRatingMapper passengerRatingMapper;
//
//    @InjectMocks
//    PassengerRatingController passengerRatingController;
//
//    @BeforeEach
//    void setUp() {
//        passengerRatingMapper = PassengerRatingMapper.INSTANCE;
//    }
//
//    private List<PassengerRating> initPassengerRatings() {
//        return List.of(
//                new PassengerRating("1",
//                        new Passenger("1", "name", "name@gmail.com",
//                                "+375299388823", false),
//                        100f, 30),
//                new PassengerRating("2",
//                        new Passenger("2", "name1", "name1@gmail.com",
//                                "+375299388824", false),
//                        90f, 25)
//        );
//    }
//
//    @Test
//    @DisplayName("Getting all passenger ratings.")
//    void getAllPassengerRatingsTest_ReturnsValidResponseEntity() {
//        //given
//        List<PassengerRating> passengerRatings = initPassengerRatings();
//        doReturn(passengerRatings).when(this.passengerRatingService).getAllPassengerRatings(true);
//
//        //when
//        ResponseEntity<List<PassengerRating>> responseEntity = passengerRatingController
//                .getAllPassengerRatings(true);
//
//        //then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(passengerRatings, responseEntity.getBody());
//    }
//
//    @Test
//    @DisplayName("Getting all not deleted passenger ratings.")
//    void getAllNotDeletedPassengerRatingsTest_ReturnsValidResponseEntity() {
//        //given
//        List<PassengerRating> passengerRatings = initPassengerRatings();
//        doReturn(passengerRatings).when(this.passengerRatingService).getAllPassengerRatings(false);
//
//        //when
//        ResponseEntity<List<PassengerRating>> responseEntity = passengerRatingController
//                .getAllPassengerRatings(false);
//
//        //then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(passengerRatings, responseEntity.getBody());
//    }
//
//    @Test
//    @DisplayName("Getting passenger rating by id.")
//    void getPassengerRatingByIdTest_ReturnsValidResponseEntity() {
//        //given
//        String passengerRatingId = "1";
//        PassengerRating passengerRating = new PassengerRating(passengerRatingId,
//                new Passenger("1", "name", "name@gmail.com",
//                        "+375299388823", false),
//                100f, 30);
//        doReturn(passengerRating).when(this.passengerRatingService).getPassengerRatingById(passengerRatingId);
//
//        //when
//        ResponseEntity<PassengerRating> responseEntity = passengerRatingController
//                .getPassengerRatingById(passengerRatingId);
//
//        //then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(passengerRating, responseEntity.getBody());
//    }
//
//    @Test
//    @DisplayName("Getting passenger rating by passenger id.")
//    void getPassengerRatingByPassengerIdTest_ReturnsValidResponseEntity() {
//        //given
//        String passengerId = "1";
//        PassengerRating passengerRating = new PassengerRating("1",
//                new Passenger(passengerId, "name", "name@gmail.com",
//                        "+375299388823", false),
//                100f, 30);
//        doReturn(passengerRating).when(this.passengerRatingService).getPassengerRatingByPassengerId(passengerId);
//
//        //when
//        ResponseEntity<PassengerRating> responseEntity = passengerRatingController
//                .getPassengerRatingByPassengerId(passengerId);
//
//        //then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(passengerRating, responseEntity.getBody());
//    }
//
//    @Test
//    @DisplayName("Update passenger rating by id")
//    void putPassengerRatingByIdTest_ReturnsValidResponseEntity() {
//        //given
//        String passengerRatingId = "1";
//        PassengerRatingPutDto passengerRatingPutDto = new PassengerRatingPutDto(
//                30f, 7);
//        PassengerRating passengerRating = new PassengerRating(passengerRatingId,
//                new Passenger("1", "name", "name@gmail.com",
//                        "+375299388823", false),
//                30f, 7);
//        doReturn(passengerRating).when(this.passengerRatingService).putPassengerRatingById(passengerRatingId,
//                passengerRatingMapper.fromPassengerRatingPutDtoToPassengerRating(passengerRatingPutDto));
//
//        //when
//        ResponseEntity<PassengerRating> responseEntity = passengerRatingController.putPassengerRatingById(
//                passengerRatingId, passengerRatingPutDto
//        );
//
//        //then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertInstanceOf(PassengerRating.class, responseEntity.getBody());
//        assertNotEquals(responseEntity.getBody().getId(), 0);
//        assertEquals(passengerRating.getPassenger(), responseEntity.getBody().getPassenger());
//        assertEquals(passengerRating.getRatingValue(), responseEntity.getBody().getRatingValue());
//        assertEquals(passengerRating.getNumberOfRatings(), responseEntity.getBody().getNumberOfRatings());
//        verify(this.passengerRatingService).putPassengerRatingById(passengerRatingId,
//                passengerRatingMapper.fromPassengerRatingPutDtoToPassengerRating(passengerRatingPutDto));
//    }
//
//    @Test
//    @DisplayName("Partially update passenger rating by id.")
//    void patchPassengerRatingByIdTest_ReturnsValidResponseEntity() {
//        //given
//        String passengerRatingId = "1";
//        PassengerRatingPatchDto passengerRatingPatchDto = new PassengerRatingPatchDto(
//                30f, 7);
//        PassengerRating passengerRating = new PassengerRating(passengerRatingId,
//                new Passenger("1", "name", "name@gmail.com",
//                        "+375299388823", false),
//                30f, 7);
//        doReturn(passengerRating).when(this.passengerRatingService).patchPassengerRatingById(passengerRatingId,
//                passengerRatingMapper.fromPassengerRatingPatchDtoToPassengerRating(passengerRatingPatchDto));
//
//        //when
//        ResponseEntity<PassengerRating> responseEntity = passengerRatingController.patchPassengerRatingById(
//                passengerRatingId, passengerRatingPatchDto
//        );
//
//        //then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertInstanceOf(PassengerRating.class, responseEntity.getBody());
//        assertNotEquals(responseEntity.getBody().getId(), 0);
//        assertEquals(passengerRating.getPassenger(), responseEntity.getBody().getPassenger());
//        assertEquals(passengerRating.getRatingValue(), responseEntity.getBody().getRatingValue());
//        assertEquals(passengerRating.getNumberOfRatings(), responseEntity.getBody().getNumberOfRatings());
//        verify(this.passengerRatingService).patchPassengerRatingById(passengerRatingId,
//                passengerRatingMapper.fromPassengerRatingPatchDtoToPassengerRating(passengerRatingPatchDto));
//    }
//}