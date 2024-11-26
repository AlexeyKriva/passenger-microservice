//package com.software.modsen.passengermicroservice.services;
//
//import com.software.modsen.passengermicroservice.entities.Passenger;
//import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
//import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
//import com.software.modsen.passengermicroservice.observer.PassengerSubject;
//import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.PASSENGER_NOT_FOUND_MESSAGE;
//import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.PASSENGER_WAS_DELETED_MESSAGE;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class PassengerServiceTest {
//    @Mock
//    PassengerRepository passengerRepository;
//
//    @Mock
//    PassengerSubject passengerSubject;
//
//    @InjectMocks
//    PassengerService passengerService;
//
//    private List<Passenger> initPassengers() {
//        return List.of(
//                new Passenger("1", "Alex", "alex@gmail.com",
//                        "+375299999999", false),
//                new Passenger("2", "Ivan", "ivan@gmail.com",
//                        "+375332929293", true));
//    }
//
//    @Test
//    @DisplayName("Getting all passengers.")
//    void getAllPassengersTest_ReturnPassengers() {
//        //given
//        List<Passenger> passengers = initPassengers();
//        doReturn(passengers).when(passengerRepository).findAll();
//
//        //when
//        List<Passenger> passengersFromDb = passengerService
//                .getAllPassengersOrPassengerByName(true, null);
//
//        //then
//        assertNotNull(passengersFromDb);
//        assertEquals(passengers, passengersFromDb);
//    }
//
//    @Test
//    @DisplayName("Getting all not deleted passengers.")
//    void getNotDeletedAllPassengersTest_ReturnsValidPassengers() {
//        //given
//        List<Passenger> passengers = List.of(initPassengers().get(0));
//        doReturn(passengers).when(passengerRepository).findAll();
//
//        //when
//        List<Passenger> passengersFromDb = passengerService
//                .getAllPassengersOrPassengerByName(false, null);
//
//        //then
//        assertNotNull(passengersFromDb);
//        assertEquals(passengers, passengersFromDb);
//    }
//
//    @Test
//    @DisplayName("Getting passenger by id.")
//    void getPassengerByIdTest_WithoutExceptions_ReturnsPassenger() {
//        //given
//        String passengerId = "1";
//        Optional<Passenger> passenger = Optional.of(new Passenger(passengerId, "Alex", "post@gmail.com",
//                "+37441234567", false));
//        doReturn(passenger).when(this.passengerRepository).findById(passengerId);
//
//        //when
//        Passenger passengerFromDb = passengerService.getPassengerById(passengerId);
//
//        //then
//        assertNotNull(passengerFromDb);
//        assertNotEquals(passengerFromDb.getId(), 0);
//        assertEquals(passenger.get().getName(), passengerFromDb.getName());
//        assertEquals(passenger.get().getEmail(), passengerFromDb.getEmail());
//        assertEquals(passenger.get().getPhoneNumber(), passengerFromDb.getPhoneNumber());
//        assertEquals(passenger.get().isDeleted(), passengerFromDb.isDeleted());
//    }
//
//    @Test
//    @DisplayName("Getting non-existing passenger by id.")
//    void getPassengerByIdTest_WithPassengerNotFoundException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE))
//                .when(this.passengerRepository).findById(passengerId);
//
//        //when
//        PassengerNotFoundException exception = assertThrows(PassengerNotFoundException.class,
//                () -> passengerService.getPassengerById(passengerId));
//
//        //then
//        assertEquals(PASSENGER_NOT_FOUND_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Saving new passenger")
//    void savePassengerTest_ReturnsSavedPassenger() {
//        //given
//        Passenger newPassenger = new Passenger("0", "Alex", "post@gmail.com",
//                "+37441234567", false);
//        Passenger passenger = new Passenger("1", "Alex", "post@gmail.com",
//                "+37441234567", false);
//        doReturn(passenger).when(this.passengerRepository).save(newPassenger);
//        doNothing().when(this.passengerSubject).notifyPassengerObservers(passenger.getId());
//
//        //when
//        Passenger passengerFromDb = passengerService.savePassenger(newPassenger);
//
//        //then
//        assertNotNull(passengerFromDb);
//        assertNotEquals(passengerFromDb.getId(), 0);
//        assertEquals(passenger.getName(), passengerFromDb.getName());
//        assertEquals(passenger.getEmail(), passengerFromDb.getEmail());
//        assertEquals(passenger.getPhoneNumber(), passengerFromDb.getPhoneNumber());
//        assertEquals(passenger.isDeleted(), passengerFromDb.isDeleted());
//        verify(this.passengerRepository).save(newPassenger);
//    }
//
//    @Test
//    @DisplayName("Updating passenger by id.")
//    void updatePassengerByIdTest_WithoutException_ReturnsUpdatedPassenger() {
//        //given
//        String passengerId = "1";
//        Optional<Passenger> optionalPassenger = Optional.of(new Passenger("1", "Alex", "post@gmail.com",
//                "+37443234567", false));
//        doReturn(optionalPassenger).when(this.passengerRepository).findById(passengerId);
//        Passenger passengerData = new Passenger(passengerId, "Alex1", "post@gmail.com",
//                "+37443234567", false);
//        doReturn(passengerData).when(this.passengerRepository).save(passengerData);
//        passengerData.setId("0");
//
//        //when
//        Passenger passengerFromDb = passengerService.updatePassengerById(passengerId, passengerData);
//
//        //then
//        assertNotNull(passengerFromDb);
//        assertNotEquals(passengerFromDb.getId(), 0);
//        assertEquals(passengerData.getName(), passengerFromDb.getName());
//        assertEquals(passengerData.getEmail(), passengerFromDb.getEmail());
//        assertEquals(passengerData.getPhoneNumber(), passengerFromDb.getPhoneNumber());
//        assertEquals(passengerData.isDeleted(), passengerFromDb.isDeleted());
//        verify(this.passengerRepository).save(passengerData);
//    }
//
//    @Test
//    @DisplayName("Updating non-existent passenger by id")
//    void updatePassengerByIdTest_WithPassengerNotFoundException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE))
//                .when(this.passengerRepository).findById("1");
//        Passenger passengerData = new Passenger("0", "Alex", "post@gmail.com",
//                "+37443234567", false);
//
//        //when
//        PassengerNotFoundException exception = assertThrows(PassengerNotFoundException.class, () ->
//                passengerService.updatePassengerById(passengerId, passengerData));
//
//        //then
//        assertEquals(PASSENGER_NOT_FOUND_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Updating deleted passenger by id")
//    void updatePassengerByIdTest_WithPassengerWasDeletedException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        Optional<Passenger> optionalPassenger = Optional.of(new Passenger(passengerId, "Alex",
//                "post@gmail.com", "+37443234567", true));
//        doReturn(optionalPassenger)
//                .when(this.passengerRepository).findById("1");
//        Passenger passengerData = new Passenger("0", "Alex1", "post1@gmail.com",
//                "+375291234567", false);
//
//        //when
//        PassengerWasDeletedException exception = assertThrows(PassengerWasDeletedException.class, () ->
//                passengerService.updatePassengerById(passengerId, passengerData));
//
//        //then
//        assertEquals(PASSENGER_WAS_DELETED_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Partially updating passenger by id.")
//    void patchPassengerByIdTest_WithoutException_ReturnsPatchedPassenger() {
//        //given
//        String passengerId = "1";
//        Optional<Passenger> optionalPassenger = Optional.of(new Passenger("1", "Alex", "post@gmail.com",
//                "+37443234567", false));
//        doReturn(optionalPassenger).when(this.passengerRepository).findById(passengerId);
//        Passenger passengerData = new Passenger("0", "Alex1", "post1@gmail.com",
//                null, false);
//        passengerData.setId(passengerId);
//        passengerData.setPhoneNumber(optionalPassenger.get().getPhoneNumber());
//        doReturn(passengerData).when(this.passengerRepository).save(passengerData);
//        passengerData.setId("0");
//        passengerData.setPhoneNumber(null);
//
//        //when
//        Passenger passengerFromDb = passengerService.patchPassengerById(passengerId, passengerData);
//
//        //then
//        assertNotNull(passengerFromDb);
//        assertNotEquals(passengerFromDb.getId(), 0);
//        assertEquals(passengerData.getName(), passengerFromDb.getName());
//        assertEquals(passengerData.getEmail(), passengerFromDb.getEmail());
//        assertEquals(passengerData.getPhoneNumber(), passengerFromDb.getPhoneNumber());
//        assertEquals(passengerData.isDeleted(), passengerFromDb.isDeleted());
//        verify(this.passengerRepository).save(passengerData);
//    }
//
//    @Test
//    @DisplayName("Partially updating non-existent passenger by id.")
//    void patchPassengerByIdTest_WithPassengerNotFoundException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE))
//                .when(this.passengerRepository).findById(passengerId);
//        Passenger passengerData = new Passenger("0", "Alex1", "post1@gmail.com",
//                null, false);
//
//        //when
//        PassengerNotFoundException exception = assertThrows(PassengerNotFoundException.class, () ->
//                passengerService.patchPassengerById(passengerId, passengerData));
//
//        //then
//        assertEquals(PASSENGER_NOT_FOUND_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Updating deleted passenger by id")
//    void patchPassengerByIdTest_WithPassengerWasDeletedException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        Optional<Passenger> optionalPassenger = Optional.of(new Passenger(passengerId, "Alex",
//                "post@gmail.com", "+37443234567", true));
//        doReturn(optionalPassenger)
//                .when(this.passengerRepository).findById("1");
//        Passenger passengerData = new Passenger("0", "Alex1", "post1@gmail.com",
//                null, false);
//
//        //when
//        PassengerWasDeletedException exception = assertThrows(PassengerWasDeletedException.class, () ->
//                passengerService.updatePassengerById(passengerId, passengerData));
//
//        //then
//        assertEquals(PASSENGER_WAS_DELETED_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Soft deleting passenger by id")
//    void softDeletePassengerByIdTest_WithoutException_ReturnsSoftDeletedPassenger() {
//        //given
//        String passengerId = "1";
//        Optional<Passenger> foundPassenger = Optional.of(new Passenger(passengerId, "Alex",
//                "post@gmail.com", "+37443234567", false));
//        doReturn(foundPassenger).when(this.passengerRepository).findById(passengerId);
//        Passenger deletingPassenger = foundPassenger.get();
//        deletingPassenger.setDeleted(true);
//        doReturn(deletingPassenger).when(this.passengerRepository).save(deletingPassenger);
//
//        //when
//        Passenger passengerFromDb = passengerService.softDeletePassengerById(passengerId);
//
//        //then
//        assertNotNull(passengerFromDb);
//        assertNotEquals(passengerFromDb.getId(), 0);
//        assertEquals(deletingPassenger.getName(), passengerFromDb.getName());
//        assertEquals(deletingPassenger.getEmail(), passengerFromDb.getEmail());
//        assertEquals(deletingPassenger.getPhoneNumber(), passengerFromDb.getPhoneNumber());
//        assertEquals(deletingPassenger.isDeleted(), passengerFromDb.isDeleted());
//        verify(this.passengerRepository).save(deletingPassenger);
//    }
//
//    @Test
//    @DisplayName("Soft deleting non-existent passenger by id")
//    void softDeletePassengerByIdTest_WithPassengerNotFoundException_ReturnsException() {
//        //given
//        String passengerId = "2";
//        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE))
//                .when(this.passengerRepository).findById(passengerId);
//
//        //when
//        PassengerNotFoundException exception = assertThrows(PassengerNotFoundException.class,
//                () -> passengerService.softDeletePassengerById(passengerId));
//
//        //then
//        assertEquals(PASSENGER_NOT_FOUND_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Soft recovering passenger by id")
//    void softRecoveryPassengerByIdTest_WithoutException_ReturnsSoftRecoveryPassenger() {
//        //given
//        String passengerId = "1";
//        Optional<Passenger> foundPassenger = Optional.of(new Passenger(passengerId, "Alex",
//                "post@gmail.com", "+37443234567", true));
//        doReturn(foundPassenger).when(this.passengerRepository).findById(passengerId);
//        Passenger recoveringPassenger = foundPassenger.get();
//        recoveringPassenger.setDeleted(false);
//        doReturn(recoveringPassenger).when(this.passengerRepository).save(recoveringPassenger);
//
//        //when
//        Passenger passengerFromDb = passengerService.softRecoveryPassengerById(passengerId);
//
//        //then
//        assertNotNull(passengerFromDb);
//        assertNotEquals(passengerFromDb.getId(), 0);
//        assertEquals(recoveringPassenger.getName(), passengerFromDb.getName());
//        assertEquals(recoveringPassenger.getEmail(), passengerFromDb.getEmail());
//        assertEquals(recoveringPassenger.getPhoneNumber(), passengerFromDb.getPhoneNumber());
//        assertEquals(recoveringPassenger.isDeleted(), passengerFromDb.isDeleted());
//        verify(this.passengerRepository).save(recoveringPassenger);
//    }
//
//    @Test
//    @DisplayName("Soft recovering non-existent passenger by id")
//    void softRecoveryPassengerByIdTest_WithPassengerNotFoundException_ReturnsException() {
//        //given
//        String passengerId = "2";
//        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE))
//                .when(this.passengerRepository).findById(passengerId);
//
//        //when
//        PassengerNotFoundException exception = assertThrows(PassengerNotFoundException.class,
//                () -> passengerService.softRecoveryPassengerById(passengerId));
//
//        //then
//        assertEquals(PASSENGER_NOT_FOUND_MESSAGE, exception.getMessage());
//    }
//}