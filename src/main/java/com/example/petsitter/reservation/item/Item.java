package com.example.petsitter.reservation.item;

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
@Table(name = "item",
        indexes = {
                @Index(name = "item_reservation_id_idx", columnList = "reservation_id")
        }
)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    private List<String> times = new ArrayList<>();

    private String status;

    @Column(nullable = false)
    private Long timeCount;

    @Column(nullable = false)
    private Long price;


    @Builder
    public Item(Long id, Reservation reservation,List<String> times,String status, Long timeCount, Long price) {
        this.id = id;
        this.reservation = reservation;
        this.times = times;
        this.status = status;
        this.timeCount = timeCount;
        this.price = price;
    }
}
