package com.software.modsen.passengermicroservice.controllers;

import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountBalanceDownDto;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountBalanceUpDto;
import com.software.modsen.passengermicroservice.mappers.PassengerAccountMapper;
import com.software.modsen.passengermicroservice.services.PassengerAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/passenger/account", produces = "application/json")
@AllArgsConstructor
@Tag(name = "Passenger account controller", description = "Allows to interact with passenger accounts.")
public class PassengerAccountController {
    private PassengerAccountService passengerAccountRepository;
    private final PassengerAccountMapper PASSENGER_ACCOUNT_MAPPER = PassengerAccountMapper.INSTANCE;

    @GetMapping
    @Operation(
            description = "Allows to get all passenger accounts."
    )
    public ResponseEntity<List<PassengerAccount>> getAllPassengerAccounts() {
        return ResponseEntity.ok(passengerAccountRepository.getAllPassengerAccounts());
    }

    @GetMapping("/not-deleted")
    @Operation(
            description = "Allows to get all not deleted passenger accounts."
    )
    public ResponseEntity<List<PassengerAccount>> getAllNotDeletedPassengerAccounts() {
        return ResponseEntity.ok(passengerAccountRepository.getAllNotDeletedPassengerAccounts());
    }

    @GetMapping("/{id}")
    @Operation(
            description = "Allows to get not deleted passenger account by account id."
    )
    public ResponseEntity<PassengerAccount> getNotDeletedPassengerAccountsById(
            @PathVariable("id")
            @Parameter(description = "Passenger account id.")
            long id) {
        return ResponseEntity.ok(passengerAccountRepository.getPassengerAccountById(id));
    }

    @GetMapping("/{passenger_id}/by-passenger")
    @Operation(
            description = "Allows to get not deleted passenger account by account passenger id."
    )
    public ResponseEntity<PassengerAccount> getNotDeletedPassengerAccountsByPassengerId(
            @PathVariable("passenger_id")
            @Parameter(description = "Passenger id.")
            long passengerId) {
        return ResponseEntity.ok(passengerAccountRepository.getPassengerAccountByPassengerId(passengerId));
    }

    @PutMapping("/{passenger_id}/increase")
    @Operation(
            description = "Allows to increase passenger balance by passenger id."
    )
    public ResponseEntity<PassengerAccount> increaseBalanceByPassengerId(
            @PathVariable("passenger_id")
            @Parameter(description = "Passenger id.")
            long passengerId,
            @Valid
            @RequestBody
            @Parameter(description = "Entity to increase passenger balance.")
            PassengerAccountBalanceUpDto passengerAccountBalanceUpDto) {
        return ResponseEntity.ok(passengerAccountRepository.increaseBalance(
                        passengerId,
                        PASSENGER_ACCOUNT_MAPPER
                                .fromPassengerAccountIncreaseDtoToPassengerAccount(passengerAccountBalanceUpDto)));
    }

    @PutMapping("/{passenger_id}/cancel")
    @Transactional
    @Operation(
            description = "Allows to cancel passenger balance by passenger id."
    )
    public ResponseEntity<PassengerAccount> cancelBalanceByPassengerId(
            @PathVariable("passenger_id") @Parameter(description = "Passenger id.") long passengerId,
            @Valid @RequestBody @Parameter(description = "Entity to cancel passenger balance.")
            PassengerAccountBalanceDownDto passengerAccountBalanceDownDto) {
        return ResponseEntity.ok(passengerAccountRepository.cancelBalance(
                        passengerId,
                        PASSENGER_ACCOUNT_MAPPER
                                .fromPassengerAccountCancelDtoToPassengerAccount(passengerAccountBalanceDownDto)));
    }
}