package com.software.modsen.passengermicroservice.controllers;

import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountCancelDto;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountIncreaseDto;
import com.software.modsen.passengermicroservice.mappers.PassengerAccountMapper;
import com.software.modsen.passengermicroservice.services.PassengerAccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/passenger/account", produces = "application/json")
@AllArgsConstructor
public class PassengerAccountController {
    private PassengerAccountService passengerAccountRepository;
    private final PassengerAccountMapper PASSENGER_ACCOUNT_MAPPER = PassengerAccountMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<PassengerAccount>> getAllPassengerAccounts() {
        return ResponseEntity.ok(passengerAccountRepository.getAllPassengerAccounts());
    }

    @GetMapping("/not-deleted")
    public ResponseEntity<List<PassengerAccount>> getAllNotDeletedPassengerAccounts() {
        return ResponseEntity.ok(passengerAccountRepository.getAllNotDeletedPassengerAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerAccount> getNotDeletedPassengerAccountsById(@PathVariable("id") long id) {
        return ResponseEntity.ok(passengerAccountRepository.getPassengerAccountById(id));
    }

    @GetMapping("/{passenger_id}/by-passenger")
    public ResponseEntity<PassengerAccount> getNotDeletedPassengerAccountsByPassengerId(
            @PathVariable("passenger_id") long passengerId) {
        return ResponseEntity.ok(passengerAccountRepository.getPassengerAccountByPassengerId(passengerId));
    }

    @PutMapping("/{passenger_id}/increase")
    public ResponseEntity<PassengerAccount> increaseBalanceByPassengerId(
            @PathVariable("passenger_id") long passengerId,
            @Valid @RequestBody PassengerAccountIncreaseDto passengerAccountIncreaseDto) {
        return ResponseEntity.ok(passengerAccountRepository.increaseBalance(
                        passengerId,
                        PASSENGER_ACCOUNT_MAPPER.fromPassengerAccountIncreaseDtoToPassengerAccount(passengerAccountIncreaseDto)));
    }

    @PutMapping("/{passenger_id}/cancel")
    public ResponseEntity<PassengerAccount> cancelBalanceByPassengerId(
            @PathVariable("passenger_id") long passengerId,
            @Valid @RequestBody PassengerAccountCancelDto passengerAccountCancelDto) {
        return ResponseEntity.ok(passengerAccountRepository.cancelBalance(
                        passengerId,
                        PASSENGER_ACCOUNT_MAPPER.fromPassengerAccountCancelDtoToPassengerAccount(passengerAccountCancelDto)));
    }
}