package com.software.modsen.passengermicroservice.services;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.account.Currency;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerRatingNotFoundException;
import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class PassengerRatingServiceTest {
    @Mock
    PassengerRatingRepository passengerRatingRepository;

    @Mock
    PassengerRepository passengerRepository;

    @InjectMocks
    PassengerRatingService passengerRatingService;

    private List<PassengerRating> initPassengerRatings() {
        return List.of(
                new PassengerRating(1, new Passenger(1, "Alex", "post@gmail.com",
                        "+37441234567", false),
                        4.5F, 129),
                new PassengerRating(1, new Passenger(2, "Ivan", "ivan@gmail.com",
                        "+375332929293", true),
                        5.0F, 33));
    }

    @Test
    @DisplayName("Getting all passenger ratings.")
    void getAllPassengersTest_ReturnPassengerAccounts() {
        //given
        List<PassengerRating> passengerRatings = initPassengerRatings();
        doReturn(passengerRatings).when(passengerRatingRepository).findAll();

        //when
        List<PassengerRating> passengersRatingsFromDb = passengerRatingService.getAllPassengerRatings();

        //then
        assertNotNull(passengersRatingsFromDb);
        assertEquals(passengerRatings, passengersRatingsFromDb);
    }

    @Test
    @DisplayName("Getting all not deleted passengers.")
    void getAllNotDeletedPassengerAccountsTest_ReturnsValidAccounts() {
        //given
        List<PassengerRating> passengerRatings = initPassengerRatings();
        List<PassengerRating> notDeletedPassengerRatings = List.of(passengerRatings.get(0));
        doReturn(passengerRatings).when(passengerRatingRepository).findAll();
        Optional<Passenger> passengerOptional = Optional.of(notDeletedPassengerRatings.get(0).getPassenger());
        doReturn(passengerOptional).when(this.passengerRepository)
                .findPassengerByIdAndIsDeleted(notDeletedPassengerRatings.get(0).getPassenger().getId(),
                        false);

        //when
        List<PassengerRating> passengerRatingsFromDb = passengerRatingService.getAllNotDeletedPassengerRatings();

        //then
        assertNotNull(passengerRatingsFromDb);
        assertEquals(notDeletedPassengerRatings, passengerRatingsFromDb);
    }

    @Test
    @DisplayName("Getting passenger rating by id.")
    void getPassengerRatingByIdTest_WithoutException_ReturnsPassengerRating() {
        //given
        long passengerRatingId = 1;
        Optional<PassengerRating> passengerRating = Optional.of(new PassengerRating(1,
                new Passenger(1, "Alex", "post@gmail.com",
                        "+37441234567", false),
                4.5F, 129));
        doReturn(passengerRating).when(this.passengerRatingRepository).findById(passengerRatingId);

        //when
        PassengerRating passengerRatingFromDb = passengerRatingService.getPassengerRatingById(passengerRatingId);

        //then
        assertNotNull(passengerRatingFromDb);
        assertNotEquals(passengerRatingFromDb.getId(), 0);
        assertEquals(passengerRating.get().getPassenger(), passengerRatingFromDb.getPassenger());
        assertEquals(passengerRating.get().getRatingValue(), passengerRatingFromDb.getRatingValue());
        assertEquals(passengerRating.get().getNumberOfRatings(), passengerRatingFromDb.getNumberOfRatings());
    }

    @Test
    @DisplayName("Getting passenger rating by id.")
    void getPassengerRatingByIdTest_WithPassengerRatingNotFoundException_ReturnsException() {
        //given
        long passengerRatingId = 1;
        doThrow(new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE))
                .when(passengerRatingRepository).findById(passengerRatingId);

        //when
        PassengerRatingNotFoundException exception = assertThrows(PassengerRatingNotFoundException.class,
                () -> passengerRatingService.getPassengerRatingById(passengerRatingId));

        //then
        assertEquals(PASSENGER_RATING_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Getting passenger rating by passenger id.")
    void getPassengerRatingByPassengerIdTest_WithoutException_ReturnsPassengerRating() {
        //given
        long passengerId = 1;
        Optional<PassengerRating> passengerRating = Optional.of(new PassengerRating(1,
                new Passenger(1, "Alex", "post@gmail.com",
                        "+37441234567", false),
                4.5F, 129));
        doReturn(passengerRating).when(this.passengerRatingRepository).findByPassengerId(passengerId);

        //when
        PassengerRating passengerRatingFromDb = passengerRatingService.getPassengerRatingByPassengerId(passengerId);

        //then
        assertNotNull(passengerRatingFromDb);
        assertNotEquals(passengerRatingFromDb.getId(), 0);
        assertEquals(passengerRating.get().getPassenger(), passengerRatingFromDb.getPassenger());
        assertEquals(passengerRating.get().getRatingValue(), passengerRatingFromDb.getRatingValue());
        assertEquals(passengerRating.get().getNumberOfRatings(), passengerRatingFromDb.getNumberOfRatings());
    }

    @Test
    @DisplayName("Getting non-existing passenger rating by passenger id.")
    void getPassengerRatingByPassengerIdTest_WithPassengerRatingNotFoundException_ReturnsException() {
        //given
        long passengerRatingId = 1;
        doThrow(new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE))
                .when(passengerRatingRepository).findByPassengerId(passengerRatingId);

        //when
        PassengerRatingNotFoundException exception = assertThrows(PassengerRatingNotFoundException.class,
                () -> passengerRatingService.getPassengerRatingByPassengerId(passengerRatingId));

        //then
        assertEquals(PASSENGER_RATING_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Getting deleted passenger rating by passenger id.")
    void getPassengerRatingByPassengerIdTest_WithPassengerWasDeletedException_ReturnsException() {
        //given
        long passengerId = 1;
        Optional<PassengerRating> passengerRating = Optional.of(new PassengerRating(1,
                new Passenger(1, "Alex", "post@gmail.com",
                        "+37441234567", true),
                4.5F, 129));
        doReturn(passengerRating).when(this.passengerRatingRepository).findByPassengerId(passengerId);

        //when
        PassengerWasDeletedException exception = assertThrows(PassengerWasDeletedException.class,
                () -> passengerRatingService.getPassengerRatingByPassengerId(passengerId));

        //then
        assertTrue(passengerRating.get().getPassenger().isDeleted());
        assertEquals(PASSENGER_WAS_DELETED_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Update passenger rating by id.")
    void putPassengerRatingByIdTest_WithoutException_ReturnsPassengerRating() {
        //given
        long passengerRatingId = 1;
        PassengerRating passengerRatingData = new PassengerRating(0, null,
                4.7f, 29);
        Optional<PassengerRating> passengerRatingFromData = Optional.of(new PassengerRating(1, new Passenger(
                1, "Alex", "post@gmail.com", "+37441234567", false),
                2.7f, 15));
        doReturn(passengerRatingFromData).when(passengerRatingRepository).findById(passengerRatingId);
        PassengerRating updatingPassengerRating = new PassengerRating(passengerRatingId, new Passenger(
                1, "Alex", "post@gmail.com", "+37441234567", false),
                4.7f, 29);
        doReturn(updatingPassengerRating).when(passengerRatingRepository).save(updatingPassengerRating);

        //when
        PassengerRating passengerRatingFromDb = passengerRatingService.putPassengerRatingById(passengerRatingId,
                passengerRatingData);

        //then
        assertNotNull(passengerRatingFromDb);
        assertNotEquals(passengerRatingFromDb.getId(), 0);
        assertEquals(updatingPassengerRating.getPassenger(), passengerRatingFromDb.getPassenger());
        assertEquals(updatingPassengerRating.getRatingValue(), passengerRatingFromDb.getRatingValue());
        assertEquals(updatingPassengerRating.getNumberOfRatings(), passengerRatingFromDb.getNumberOfRatings());
    }

    @Test
    @DisplayName("Update non-existing passenger rating by id.")
    void putPassengerRatingByIdTest_WithPassengerRatingNotFoundException_ReturnsException(){
        //given
        long passengerRatingId = 1;
        PassengerRating passengerRatingData = new PassengerRating(0, null,
                4.7f, 29);
        doThrow(new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE))
                .when(passengerRatingRepository).findById(passengerRatingId);

        //when
        PassengerRatingNotFoundException exception = assertThrows(PassengerRatingNotFoundException.class,
                () -> passengerRatingService.putPassengerRatingById(passengerRatingId, passengerRatingData));

        //then
        assertEquals(PASSENGER_RATING_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Update deleted passenger rating by id.")
    void putPassengerRatingByIdTest_WithPassengerWasDeletedException_ReturnsException(){
        //given
        long passengerRatingId = 1;
        PassengerRating passengerRatingData = new PassengerRating(0, null,
                4.7f, 29);
        Optional<PassengerRating> passengerRatingFromData = Optional.of(new PassengerRating(1, new Passenger(
                1, "Alex", "post@gmail.com", "+37441234567", true),
                2.7f, 15));
        doReturn(passengerRatingFromData).when(passengerRatingRepository).findById(passengerRatingId);

        //when
        PassengerWasDeletedException exception = assertThrows(PassengerWasDeletedException.class,
                () -> passengerRatingService.putPassengerRatingById(passengerRatingId, passengerRatingData));

        //then
        assertTrue(passengerRatingFromData.get().getPassenger().isDeleted());
        assertEquals(PASSENGER_WAS_DELETED_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Update passenger rating by id.")
    void patchPassengerRatingByIdTest_WithoutException_ReturnsPassengerRating() {
        //given
        long passengerRatingId = 1;
        PassengerRating passengerRatingData = new PassengerRating(0, null,
                4.7f, 29);
        Optional<PassengerRating> passengerRatingFromData = Optional.of(new PassengerRating(1, new Passenger(
                1, "Alex", "post@gmail.com", "+37441234567", false),
                2.7f, 15));
        doReturn(passengerRatingFromData).when(passengerRatingRepository).findById(passengerRatingId);
        PassengerRating updatingPassengerRating = new PassengerRating(passengerRatingId, new Passenger(
                1, "Alex", "post@gmail.com", "+37441234567", false),
                4.7f, 29);
        doReturn(updatingPassengerRating).when(passengerRatingRepository).save(updatingPassengerRating);

        //when
        PassengerRating passengerRatingFromDb = passengerRatingService.patchPassengerRatingById(passengerRatingId,
                passengerRatingData);

        //then
        assertNotNull(passengerRatingFromDb);
        assertNotEquals(passengerRatingFromDb.getId(), 0);
        assertEquals(updatingPassengerRating.getPassenger(), passengerRatingFromDb.getPassenger());
        assertEquals(updatingPassengerRating.getRatingValue(), passengerRatingFromDb.getRatingValue());
        assertEquals(updatingPassengerRating.getNumberOfRatings(), passengerRatingFromDb.getNumberOfRatings());
    }

    @Test
    @DisplayName("Update non-existing passenger rating by id.")
    void patchPassengerRatingByIdTest_WithPassengerRatingNotFoundException_ReturnsException(){
        //given
        long passengerRatingId = 1;
        PassengerRating passengerRatingData = new PassengerRating(0, null,
                4.7f, 29);
        doThrow(new PassengerRatingNotFoundException(PASSENGER_RATING_NOT_FOUND_MESSAGE))
                .when(passengerRatingRepository).findById(passengerRatingId);

        //when
        PassengerRatingNotFoundException exception = assertThrows(PassengerRatingNotFoundException.class,
                () -> passengerRatingService.patchPassengerRatingById(passengerRatingId, passengerRatingData));

        //then
        assertEquals(PASSENGER_RATING_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Update deleted passenger rating by id.")
    void patchPassengerRatingByIdTest_WithPassengerWasDeletedException_ReturnsException(){
        //given
        long passengerRatingId = 1;
        PassengerRating passengerRatingData = new PassengerRating(0, null,
                4.7f, 29);
        Optional<PassengerRating> passengerRatingFromData = Optional.of(new PassengerRating(1, new Passenger(
                1, "Alex", "post@gmail.com", "+37441234567", true),
                2.7f, 15));
        doReturn(passengerRatingFromData).when(passengerRatingRepository).findById(passengerRatingId);

        //when
        PassengerWasDeletedException exception = assertThrows(PassengerWasDeletedException.class,
                () -> passengerRatingService.patchPassengerRatingById(passengerRatingId, passengerRatingData));

        //then
        assertTrue(passengerRatingFromData.get().getPassenger().isDeleted());
        assertEquals(PASSENGER_WAS_DELETED_MESSAGE, exception.getMessage());
    }
}