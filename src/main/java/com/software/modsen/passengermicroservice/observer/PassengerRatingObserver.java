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
    public void updatePassengerInfo(String passengerId) {
        passengerRepository.findById(passengerId)
                .flatMap(passenger -> {
                    PassengerRating newPassengerRating = new PassengerRating();
                    newPassengerRating.setPassengerId(passengerId);
                    newPassengerRating.setRatingValue(0.0f);
                    newPassengerRating.setNumberOfRatings(0);

                    return passengerRatingRepository.save(newPassengerRating);
                })
                .subscribe();
    }
}
