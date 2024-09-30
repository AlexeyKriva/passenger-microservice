package com.software.modsen.passengermicroservice.observer;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
public class PassengerRatingObserver implements PassengerObserver{
    private PassengerRepository passengerRepository;
    private PassengerRatingRepository passengerRatingRepository;

    @Override
    @Transactional
    public void updatePassengerInfo(long passengerId) {
        Optional<Passenger> passengerFromDb = passengerRepository.findById(passengerId);

        PassengerRating newPassengerRating = new PassengerRating();

        newPassengerRating.setPassenger(passengerFromDb.get());
        newPassengerRating.setRatingValue(0.0f);
        newPassengerRating.setNumberOfRatings(0);

        passengerRatingRepository.save(newPassengerRating);
    }
}
