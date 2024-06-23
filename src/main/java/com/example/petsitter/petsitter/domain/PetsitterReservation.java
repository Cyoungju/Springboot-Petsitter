package com.example.petsitter.petsitter.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "petsitter_reservation")
public class PetsitterReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "petsitter_id")
    private Petsitter petsitter;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public PetsitterReservation(Petsitter petsitter, Reservation reservation) {
        this.petsitter = petsitter;
        this.reservation = reservation;
    }
}
