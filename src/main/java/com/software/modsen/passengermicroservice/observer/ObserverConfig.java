package com.software.modsen.passengermicroservice.observer;

import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import com.software.modsen.passengermicroservice.repositories.PassengerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {
    @Bean
    public PassengerSubject passengerSubject(PassengerRepository passengerRepository,
                                             PassengerRatingRepository passengerRatingRepository) {
        PassengerSubject passengerSubject = new PassengerSubject();
        passengerSubject.addPassengerObserver(new PassengerRatingObserver(passengerRepository,
                passengerRatingRepository));

        return passengerSubject;
    }
}
