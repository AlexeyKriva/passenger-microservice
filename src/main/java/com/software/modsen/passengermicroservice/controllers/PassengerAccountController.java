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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping(value = "/api/passengers", produces = "application/json")
@AllArgsConstructor
@Tag(name = "Passenger account controller", description = "Allows to interact with passenger accounts.")
public class PassengerAccountController {
    private PassengerAccountService passengerAccountService;
    private final PassengerAccountMapper PASSENGER_ACCOUNT_MAPPER = PassengerAccountMapper.INSTANCE;

    @GetMapping("/accounts")
    @Operation(
            description = "Allows to get all passenger accounts."
    )
    public Flux<PassengerAccount> getAllPassengerAccounts(@RequestParam(name = "includeDeleted",
            required = false, defaultValue = "true")
                                                                              boolean includeDeleted) {
        return passengerAccountService.getAllPassengerAccounts(includeDeleted);
    }

    @GetMapping("/accounts/{id}")
    @Operation(
            description = "Allows to get not deleted passenger account by account id."
    )
    public Mono<PassengerAccount> getNotDeletedPassengerAccountsById(
            @PathVariable("id")
            @Parameter(description = "Passenger account id.")
            String id) {
        return passengerAccountService.getPassengerAccountById(id);
    }

    @GetMapping("/{passenger_id}/accounts")
    @Operation(
            description = "Allows to get not deleted passenger account by account passenger id."
    )
    public Mono<PassengerAccount> getNotDeletedPassengerAccountsByPassengerId(
            @PathVariable("passenger_id")
            @Parameter(description = "Passenger id.")
            String passengerId) {
        return passengerAccountService.getPassengerAccountByPassengerId(passengerId);
    }

    @PutMapping("/{passenger_id}/accounts/up")
    @Operation(
            description = "Allows to increase passenger balance by passenger id."
    )
    public Mono<PassengerAccount> increaseBalanceByPassengerId(
            @PathVariable("passenger_id")
            @Parameter(description = "Passenger id.")
            String passengerId,
            @Valid
            @RequestBody
            @Parameter(description = "Entity to increase passenger balance.")
            PassengerAccountBalanceUpDto passengerAccountBalanceUpDto) {
        return passengerAccountService.increaseBalance(
                        passengerId,
                        PASSENGER_ACCOUNT_MAPPER
                                .fromPassengerAccountIncreaseDtoToPassengerAccount(passengerAccountBalanceUpDto));
    }

    @PutMapping("/{passenger_id}/accounts/down")
    @Operation(
            description = "Allows to cancel passenger balance by passenger id."
    )
    public Mono<PassengerAccount> cancelBalanceByPassengerId(
            @PathVariable("passenger_id") @Parameter(description = "Passenger id.") String passengerId,
            @Valid @RequestBody @Parameter(description = "Entity to cancel passenger balance.")
            PassengerAccountBalanceDownDto passengerAccountBalanceDownDto) {
        return passengerAccountService.cancelBalance(
                        passengerId,
                        PASSENGER_ACCOUNT_MAPPER
                                .fromPassengerAccountCancelDtoToPassengerAccount(passengerAccountBalanceDownDto));
    }
}