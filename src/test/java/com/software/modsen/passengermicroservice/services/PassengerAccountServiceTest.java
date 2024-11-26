//package com.software.modsen.passengermicroservice.services;
//
//import com.software.modsen.passengermicroservice.entities.Passenger;
//import com.software.modsen.passengermicroservice.entities.account.Currency;
//import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
//import com.software.modsen.passengermicroservice.exceptions.InsufficientAccountBalanceException;
//import com.software.modsen.passengermicroservice.exceptions.PassengerAccountNotFoundException;
//import com.software.modsen.passengermicroservice.exceptions.PassengerNotFoundException;
//import com.software.modsen.passengermicroservice.exceptions.PassengerWasDeletedException;
//import com.software.modsen.passengermicroservice.repositories.PassengerAccountRepository;
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
//import static com.software.modsen.passengermicroservice.exceptions.ErrorMessage.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.doThrow;
//
//@ExtendWith(MockitoExtension.class)
//public class PassengerAccountServiceTest {
//    @Mock
//    PassengerAccountRepository passengerAccountRepository;
//
//    @Mock
//    PassengerRepository passengerRepository;
//
//    @InjectMocks
//    PassengerAccountService passengerAccountService;
//
//    private List<PassengerAccount> initPassengerAccounts() {
//        return List.of(
//                new PassengerAccount("1", new Passenger("1", "Alex", "post@gmail.com",
//                        "+37441234567", false),
//                        100f, Currency.BYN, 0L),
//                new PassengerAccount("1", new Passenger("2", "Ivan", "ivan@gmail.com",
//                        "+375332929293", true),
//                        100f, Currency.BYN, 0L));
//    }
//
//    @Test
//    @DisplayName("Getting all passenger accounts.")
//    void getAllPassengerAccountsTest_ReturnPassengerAccounts() {
//        //given
//        List<PassengerAccount> passengerAccounts = initPassengerAccounts();
//        doReturn(passengerAccounts).when(passengerAccountRepository).findAll();
//
//        //when
//        List<PassengerAccount> passengersAccountsFromDb = passengerAccountService
//                .getAllPassengerAccounts(true);
//
//        //then
//        assertNotNull(passengersAccountsFromDb);
//        assertEquals(passengerAccounts, passengersAccountsFromDb);
//    }
//
//    @Test
//    @DisplayName("Getting all not deleted passenger accounts.")
//    void getAllNotDeletedPassengerAccountsTest_ReturnsValidPassengerAccounts() {
//        //given
//        List<PassengerAccount> passengerAccounts = initPassengerAccounts();
//        List<PassengerAccount> notDeletedPassengerAccounts = List.of(passengerAccounts.get(0));
//        doReturn(notDeletedPassengerAccounts).when(this.passengerAccountRepository).findAll();
//        doReturn(true).when(this.passengerRepository)
//                .existsByIdAndDeleted(notDeletedPassengerAccounts.get(0).getPassenger().getId(),
//                        false);
//
//        //when
//        List<PassengerAccount> passengerAccountsFromDb = passengerAccountService
//                .getAllPassengerAccounts(false);
//
//        //then
//        assertNotNull(passengerAccountsFromDb);
//        assertEquals(notDeletedPassengerAccounts, passengerAccountsFromDb);
//    }
//
//    @Test
//    @DisplayName("Getting passenger account by id.")
//    void getPassengerAccountByIdTest_WithoutExceptions_ReturnsValidPassengerAccount() {
//        //given
//        String passengerAccountId = "1";
//        Optional<PassengerAccount> passengerAccount = Optional.of(new PassengerAccount("1", new Passenger("1",
//                "Alex", "post@gmail.com", "+37441234567", false),
//                100f, Currency.BYN, 0L));
//        doReturn(passengerAccount).when(this.passengerAccountRepository).findById(passengerAccountId);
//
//        //when
//        PassengerAccount passengerAccountFromDb = passengerAccountService.getPassengerAccountById(passengerAccountId);
//
//        //then
//        assertNotNull(passengerAccountFromDb);
//        assertNotEquals(passengerAccountFromDb.getId(), 0);
//        assertEquals(passengerAccount.get().getPassenger(), passengerAccountFromDb.getPassenger());
//        assertEquals(passengerAccount.get().getBalance(), passengerAccountFromDb.getBalance());
//        assertEquals(passengerAccount.get().getCurrency(), passengerAccountFromDb.getCurrency());
//    }
//
//    @Test
//    @DisplayName("Getting non-existing passenger account by id.")
//    void getPassengerAccountByIdTest_WithPassengerAccountNotFoundException_ReturnsException() {
//        //given
//        String passengerAccountId = "1";
//        doThrow(new PassengerAccountNotFoundException(PASSENGER_ACCOUNT_NOT_FOUND_MESSAGE))
//                .when(this.passengerAccountRepository).findById(passengerAccountId);
//
//        //when
//        PassengerAccountNotFoundException exception = assertThrows(PassengerAccountNotFoundException.class,
//                () -> passengerAccountService.getPassengerAccountById(passengerAccountId));
//
//        //then
//        assertEquals(PASSENGER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Getting passenger account by id.")
//    void getPassengerAccountByPassengerIdTest_WithoutException_ReturnsPassengerAccount() {
//        //given
//        String passengerId = "1";
//        Optional<PassengerAccount> passengerAccount = Optional.of(new PassengerAccount("1", new Passenger(passengerId,
//                "Alex", "post@gmail.com", "+37441234567", false),
//                100f, Currency.BYN, 0L));
//        doReturn(passengerAccount).when(this.passengerAccountRepository).findByPassengerId(passengerId);
//
//        //when
//        PassengerAccount passengerAccountFromDb = passengerAccountService.getPassengerAccountByPassengerId(passengerId);
//
//        //then
//        assertNotNull(passengerAccountFromDb);
//        assertNotEquals(passengerAccountFromDb.getId(), 0);
//        assertEquals(passengerAccount.get().getPassenger(), passengerAccountFromDb.getPassenger());
//        assertEquals(passengerAccount.get().getBalance(), passengerAccountFromDb.getBalance());
//        assertEquals(passengerAccount.get().getCurrency(), passengerAccountFromDb.getCurrency());
//    }
//
//    @Test
//    @DisplayName("Getting non-existing passenger account by id.")
//    void getPassengerAccountByPassengerIdTest_WithPassengerAccountNotFoundException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        doThrow(new PassengerAccountNotFoundException(PASSENGER_ACCOUNT_NOT_FOUND_MESSAGE))
//                .when(this.passengerAccountRepository).findByPassengerId(passengerId);
//
//        //when
//        PassengerAccountNotFoundException exception = assertThrows(PassengerAccountNotFoundException.class,
//                () -> passengerAccountService.getPassengerAccountByPassengerId(passengerId));
//
//        //then
//        assertEquals(PASSENGER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Getting deleted passenger account by passenger id.")
//    void getPassengerAccountByPassengerIdTest_WithPassengerWasDeletedException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        Optional<PassengerAccount> passengerAccount = Optional.of(new PassengerAccount("1", new Passenger("1",
//                "Alex", "post@gmail.com", "+37441234567", true),
//                100f, Currency.BYN, 0L));
//        doReturn(passengerAccount).when(this.passengerAccountRepository).findByPassengerId(passengerId);
//
//        //when
//        PassengerWasDeletedException exception = assertThrows(PassengerWasDeletedException.class,
//                () -> passengerAccountService.getPassengerAccountByPassengerId(passengerId));
//
//        //then
//        assertTrue(passengerAccount.get().getPassenger().isDeleted());
//        assertEquals(PASSENGER_WAS_DELETED_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Increasing passenger account balance")
//    void increaseBalanceTest_WithoutException_ReturnsPassengerAccount() {
//        //given
//        String passengerId = "1";
//        PassengerAccount addingPassengerAccount = new PassengerAccount("0", null, 50f, Currency.BYN, 0L);
//        Optional<PassengerAccount> passengerAccountOptional = Optional.of(new PassengerAccount(
//                "1", new Passenger("1", "Alex", "post@gmail.com", "+37441234567",
//                false), 100f, Currency.BYN, 0L));
//        doReturn(passengerAccountOptional).when(passengerAccountRepository).findByPassengerId(passengerId);
//        PassengerAccount finalPassengerAccount = new PassengerAccount(
//                "1", new Passenger("1", "Alex", "post@gmail.com", "+37441234567",
//                false), 150f, Currency.BYN, 0L);
//        doReturn(finalPassengerAccount).when(this.passengerAccountRepository).save(finalPassengerAccount);
//
//        //when
//        PassengerAccount passengerAccountFromDb = passengerAccountService.increaseBalance(passengerId,
//                addingPassengerAccount);
//
//        //then
//        assertFalse(passengerAccountOptional.get().getPassenger().isDeleted());
//        assertNotNull(passengerAccountFromDb);
//        assertNotEquals(passengerAccountFromDb.getId(), 0);
//        assertEquals(finalPassengerAccount.getPassenger(), passengerAccountFromDb.getPassenger());
//        assertEquals(finalPassengerAccount.getBalance(), passengerAccountFromDb.getBalance());
//        assertEquals(finalPassengerAccount.getCurrency(), passengerAccountFromDb.getCurrency());
//    }
//
//    @Test
//    @DisplayName("Increasing non-existing passenger account balance")
//    void increaseBalanceTest_WithPassengerNotFoundException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        PassengerAccount addingPassengerAccount = new PassengerAccount("0", null, 50f, Currency.BYN, 0L);
//        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE))
//                .when(passengerAccountRepository).findByPassengerId(passengerId);
//
//        //when
//        PassengerNotFoundException exception = assertThrows(PassengerNotFoundException.class,
//                () -> passengerAccountService.increaseBalance(passengerId, addingPassengerAccount));
//
//        //then
//        assertEquals(PASSENGER_NOT_FOUND_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Increasing deleted passenger account balance")
//    void increaseBalanceTest_WithPassengerWasDeletedException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        PassengerAccount addingPassengerAccount = new PassengerAccount("0", null, 50f, Currency.BYN, 0L);
//        Optional<PassengerAccount> passengerAccountOptional = Optional.of(new PassengerAccount(
//                "1", new Passenger("1", "Alex", "post@gmail.com", "+37441234567",
//                true), 100f, Currency.BYN, 0L));
//        doReturn(passengerAccountOptional).when(passengerAccountRepository).findByPassengerId(passengerId);
//
//        //when
//        PassengerWasDeletedException exception = assertThrows(PassengerWasDeletedException.class,
//                () -> passengerAccountService.increaseBalance(passengerId, addingPassengerAccount));
//
//        //then
//        assertTrue(passengerAccountOptional.get().getPassenger().isDeleted());
//        assertEquals(PASSENGER_WAS_DELETED_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Cancel passenger account balance")
//    void cancelBalanceTest_WithoutExceptions_ReturnsValidBalance() {
//        //given
//        String passengerId = "1";
//        PassengerAccount addingPassengerAccount = new PassengerAccount("0", null, 50f, Currency.BYN, 0L);
//        Optional<PassengerAccount> passengerAccountOptional = Optional.of(new PassengerAccount(
//                "1", new Passenger("1", "Alex", "post@gmail.com", "+37441234567",
//                false), 100f, Currency.BYN, 0L));
//        doReturn(passengerAccountOptional).when(passengerAccountRepository).findByPassengerId(passengerId);
//        PassengerAccount finalPassengerAccount = new PassengerAccount(
//                "1", new Passenger("1", "Alex", "post@gmail.com", "+37441234567",
//                false), 50f, Currency.BYN, 0L);
//        doReturn(finalPassengerAccount).when(this.passengerAccountRepository).save(finalPassengerAccount);
//
//        //when
//        PassengerAccount passengerAccountFromDb = passengerAccountService.cancelBalance(passengerId,
//                addingPassengerAccount);
//
//        //then
//        assertFalse(passengerAccountOptional.get().getPassenger().isDeleted());
//        assertNotNull(passengerAccountFromDb);
//        assertNotEquals(passengerAccountFromDb.getId(), 0);
//        assertEquals(finalPassengerAccount.getPassenger(), passengerAccountFromDb.getPassenger());
//        assertEquals(finalPassengerAccount.getBalance(), passengerAccountFromDb.getBalance());
//        assertEquals(finalPassengerAccount.getCurrency(), passengerAccountFromDb.getCurrency());
//    }
//
//    @Test
//    @DisplayName("Cancel non-existing passenger account balance")
//    void cancelBalanceTest_WithPassengerNotFoundException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        PassengerAccount addingPassengerAccount = new PassengerAccount("0", null, 50f, Currency.BYN, 0L);
//        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE))
//                .when(passengerAccountRepository).findByPassengerId(passengerId);
//
//        //when
//        PassengerNotFoundException exception = assertThrows(PassengerNotFoundException.class,
//                () -> passengerAccountService.cancelBalance(passengerId, addingPassengerAccount));
//
//        //then
//        assertEquals(PASSENGER_NOT_FOUND_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Cancel deleted passenger account balance")
//    void cancelBalanceTest_WithPassengerWasDeletedException_ReturnsPassengerAccount() {
//        //given
//        String passengerId = "1";
//        PassengerAccount addingPassengerAccount = new PassengerAccount("0", null, 50f, Currency.BYN, 0L);
//        Optional<PassengerAccount> passengerAccountOptional = Optional.of(new PassengerAccount(
//                "1", new Passenger("1", "Alex", "post@gmail.com", "+37441234567",
//                true), 100f, Currency.BYN, 0L));
//        doReturn(passengerAccountOptional).when(passengerAccountRepository).findByPassengerId(passengerId);
//
//        //when
//        PassengerWasDeletedException exception = assertThrows(PassengerWasDeletedException.class,
//                () -> passengerAccountService.cancelBalance(passengerId, addingPassengerAccount));
//
//        //then
//        assertTrue(passengerAccountOptional.get().getPassenger().isDeleted());
//        assertEquals(PASSENGER_WAS_DELETED_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Cancel passenger account balance by an amount greater than what is in the account")
//    void cancelBalanceTest_WithInsufficientAccountBalanceExceptionException_ReturnsException() {
//        //given
//        String passengerId = "1";
//        PassengerAccount addingPassengerAccount = new PassengerAccount("0", null, 150f, Currency.BYN, 0L);
//        Optional<PassengerAccount> passengerAccountOptional = Optional.of(new PassengerAccount(
//                "1", new Passenger("1", "Alex", "post@gmail.com", "+37441234567",
//                false), 100f, Currency.BYN, 0L));
//        doReturn(passengerAccountOptional).when(passengerAccountRepository).findByPassengerId(passengerId);
//
//        //when
//        InsufficientAccountBalanceException exception = assertThrows(InsufficientAccountBalanceException.class,
//                () -> passengerAccountService.cancelBalance(passengerId, addingPassengerAccount));
//
//        //then
//        assertTrue(addingPassengerAccount.getBalance() > passengerAccountOptional.get().getBalance());
//        assertEquals(INSUFFICIENT_ACCOUNT_BALANCE_EXCEPTION, exception.getMessage());
//    }
//}