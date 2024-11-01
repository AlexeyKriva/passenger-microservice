package com.software.modsen.passengermicroservice.controllers;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.account.Currency;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountBalanceDownDto;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountBalanceUpDto;
import com.software.modsen.passengermicroservice.mappers.PassengerAccountMapper;
import com.software.modsen.passengermicroservice.services.PassengerAccountService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PassengerAccountControllerTest {
    @Mock
    PassengerAccountService passengerAccountService;

    @Mock
    PassengerAccountMapper passengerAccountMapper;

    @InjectMocks
    PassengerAccountController passengerAccountController;

    @BeforeEach
    void setUp() {
        passengerAccountMapper = PassengerAccountMapper.INSTANCE;
    }

    private List<PassengerAccount> initPassengerAccounts() {
        return List.of(
                new PassengerAccount(1,
                        new Passenger(1, "name", "name@gmail.com",
                                "+375299388823", false),
                        100f, Currency.BYN),
                new PassengerAccount(2,
                        new Passenger(2, "name1", "name1@gmail.com",
                                "+375299388824", true),
                        90f, Currency.BYN)
        );
    }

    @Test
    @DisplayName("Getting all of passenger accounts.")
    void getAllPassengerAccountsTest_ReturnsValidResponseEntity() {
        //given
        List<PassengerAccount> passengerAccounts = initPassengerAccounts();
        doReturn(passengerAccounts).when(this.passengerAccountService).getAllPassengerAccounts(true);

        //when
        ResponseEntity<List<PassengerAccount>> responseEntity = passengerAccountController
                .getAllPassengerAccounts(true);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(passengerAccounts, responseEntity.getBody());
    }

    @Test
    @DisplayName("Getting all not deleted of passenger accounts.")
    void getAllNotDeletedPassengerAccountsTest_ReturnsValidResponseEntity() {
        //given
        List<PassengerAccount> passengerAccounts = initPassengerAccounts();
        doReturn(passengerAccounts).when(this.passengerAccountService).getAllPassengerAccounts(false);

        //when
        ResponseEntity<List<PassengerAccount>> responseEntity =
                passengerAccountController.getAllPassengerAccounts(false);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(passengerAccounts, responseEntity.getBody());
    }

    @Test
    @DisplayName("Getting passenger account by id.")
    void getNotDeletedPassengerAccountsByIdTest_ReturnsValidResponseEntity() {
        //given
        int passengerAccountId = 1;
        PassengerAccount passengerAccount = new PassengerAccount(passengerAccountId,
                new Passenger(1, "name", "name@gmail.com",
                        "+375299388823", false),
                100f, Currency.BYN);
        doReturn(passengerAccount).when(this.passengerAccountService).getPassengerAccountById(passengerAccountId);

        //when
        ResponseEntity<PassengerAccount> responseEntity = passengerAccountController
                .getNotDeletedPassengerAccountsById(passengerAccountId);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(passengerAccount, responseEntity.getBody());
    }

    @Test
    @DisplayName("Getting not deleted accounts by passenger id.")
    void getNotDeletedPassengerAccountsByPassengerIdTest_ReturnsValidResponseEntity() {
        //given
        int passengerId = 1;
        PassengerAccount passengerAccount = new PassengerAccount(1,
                new Passenger(passengerId, "name", "name@gmail.com",
                        "+375299388823", false),
                100f, Currency.BYN);
        doReturn(passengerAccount).when(this.passengerAccountService).getPassengerAccountByPassengerId(passengerId);

        //when
        ResponseEntity<PassengerAccount> responseEntity = passengerAccountController
                .getNotDeletedPassengerAccountsByPassengerId(passengerId);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(passengerAccount, responseEntity.getBody());
    }

    @Test
    @DisplayName("Increase passenger balance by passenger id.")
    void increaseBalanceByPassengerIdTest_ReturnsValidResponseEntity() {
        //given
        int passengerId = 1;
        PassengerAccountBalanceUpDto passengerAccountBalanceUpDto = new PassengerAccountBalanceUpDto(1000f,
                Currency.BYN);
        PassengerAccount passengerAccount = new PassengerAccount(1,
                new Passenger(passengerId, "name", "name@gmail.com",
                        "+375299388823", false),
                1100f, Currency.BYN);
        doReturn(passengerAccount).when(this.passengerAccountService).increaseBalance(passengerId,
                passengerAccountMapper.fromPassengerAccountIncreaseDtoToPassengerAccount(passengerAccountBalanceUpDto));

        //when
        ResponseEntity<PassengerAccount> responseEntity = passengerAccountController
                .increaseBalanceByPassengerId(passengerId, passengerAccountBalanceUpDto);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(PassengerAccount.class, responseEntity.getBody());
        assertNotEquals(responseEntity.getBody().getId(), 0);
        assertEquals(passengerAccount.getPassenger(), responseEntity.getBody().getPassenger());
        assertEquals(passengerAccount.getBalance(), responseEntity.getBody().getBalance());
        assertEquals(passengerAccount.getCurrency(), responseEntity.getBody().getCurrency());
        verify(this.passengerAccountService).increaseBalance(passengerId,
                passengerAccountMapper.fromPassengerAccountIncreaseDtoToPassengerAccount(passengerAccountBalanceUpDto));
    }

    @Test
    @DisplayName("Cancel passenger balance by passenger id.")
    void cancelBalanceByPassengerIdTest_ReturnsValidResponseEntity() {
        //given
        int passengerId = 1;
        PassengerAccountBalanceDownDto passengerAccountBalanceDownDto = new PassengerAccountBalanceDownDto(1000f,
                Currency.BYN);
        PassengerAccount passengerAccount = new PassengerAccount(1,
                new Passenger(passengerId, "name", "name@gmail.com",
                        "+375299388823", false),
                100f, Currency.BYN);
        doReturn(passengerAccount).when(this.passengerAccountService).cancelBalance(passengerId,
                passengerAccountMapper.fromPassengerAccountCancelDtoToPassengerAccount(passengerAccountBalanceDownDto));

        //when
        ResponseEntity<PassengerAccount> responseEntity = passengerAccountController
                .cancelBalanceByPassengerId(passengerId, passengerAccountBalanceDownDto);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(PassengerAccount.class, responseEntity.getBody());
        assertNotEquals(responseEntity.getBody().getId(), 0);
        assertEquals(passengerAccount.getPassenger(), responseEntity.getBody().getPassenger());
        assertEquals(passengerAccount.getBalance(), responseEntity.getBody().getBalance());
        assertEquals(passengerAccount.getCurrency(), responseEntity.getBody().getCurrency());
        verify(this.passengerAccountService).cancelBalance(passengerId,
                passengerAccountMapper.fromPassengerAccountCancelDtoToPassengerAccount(passengerAccountBalanceDownDto));
    }
}