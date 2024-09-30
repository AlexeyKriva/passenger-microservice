package com.software.modsen.passengermicroservice.entities.account;

import com.software.modsen.passengermicroservice.entities.Passenger;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passenger_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @Column(name = "balance", nullable = false)
    private Float balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
}
