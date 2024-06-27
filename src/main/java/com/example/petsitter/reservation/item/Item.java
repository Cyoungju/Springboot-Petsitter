package com.example.petsitter.reservation.item;

import com.example.petsitter.board.domain.Comment;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.domain.PetsitterReservation;
import com.example.petsitter.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    private List<String> times = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "petsitterReservation_id")
    private PetsitterReservation petsitter;

    @Column(nullable = false)
    private Long timeCount;

    @Column(nullable = false)
    private Long price;


    @Builder
    public Item(Long id, Reservation reservation,List<String> times, PetsitterReservation petsitter, Long timeCount, Long price) {
        this.id = id;
        this.reservation = reservation;
        this.times = times;
        this.petsitter = petsitter;
        this.timeCount = timeCount;
        this.price = price;
    }
}
