package com.software.modsen.passengermicroservice.observer;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.account.Currency;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.repositories.PassengerAccountRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
public class PassengerAccountObserver implements PassengerObserver {
    private PassengerRepository passengerRepository;
    private PassengerAccountRepository passengerAccountRepository;

    @Override
    @Transactional
    public void updatePassengerInfo(long passengerId) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(passengerId);

        PassengerAccount newPassengerAccount = new PassengerAccount();

        newPassengerAccount.setPassenger(passengerFromDb.get());
        newPassengerAccount.setBalance(0.0f);
        newPassengerAccount.setCurrency(Currency.BYN);

        passengerAccountRepository.save(newPassengerAccount);
    }
}
