package com.example.petsitter.reservation.domain;


import com.example.petsitter.petsitter.domain.PetsitterReservation;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "reservation_times", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "reservation_time")
    private List<LocalTime> times = new ArrayList<>();

    private Long totalPrice;


    @OneToMany(mappedBy = "reservation")
    private List<PetsitterReservation> petsitters = new ArrayList<>();


    @Builder
    public Reservation(Long id, LocalDate date, List<LocalTime> times, Long totalPrice , List<PetsitterReservation> petsitters) {
        this.id = id;
        this.date = date;
        this.times = times;
        this.totalPrice = totalPrice;
        this.petsitters = petsitters;
    }


}
