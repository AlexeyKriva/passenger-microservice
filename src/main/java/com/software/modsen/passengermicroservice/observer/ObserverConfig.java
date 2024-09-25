package com.software.modsen.passengermicroservice.observer;

import com.software.modsen.passengermicroservice.repositories.PassengerRatingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {
    @Bean
    public PassengerSubject passengerSubject(PassengerRatingRepository passengerRatingRepository) {
        PassengerSubject passengerSubject = new PassengerSubject();
        passengerSubject.addPassengerObserver(new PassengerRatingObserver(passengerRatingRepository));

        return passengerSubject;
    }
}
