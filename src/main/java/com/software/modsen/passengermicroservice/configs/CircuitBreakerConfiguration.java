package com.software.modsen.passengermicroservice.configs;

import com.software.modsen.passengermicroservice.exceptions.DatabaseConnectionRefusedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {
    @Bean(name = "simpleCircuitBreaker")
    public CircuitBreaker circuitBreakerRegistry() {
        CircuitBreakerConfig circuitBreakerConfig = new CircuitBreakerConfig.Builder()
                .failureRateThreshold(100)
                .minimumNumberOfCalls(1)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(1)
                .permittedNumberOfCallsInHalfOpenState(3)
                .slowCallDurationThreshold(Duration.ofSeconds(1))
                .recordExceptions(DatabaseConnectionRefusedException.class)
                .build();

        return CircuitBreakerRegistry.of(circuitBreakerConfig).circuitBreaker("simpleCircuitBreaker");
    }
}