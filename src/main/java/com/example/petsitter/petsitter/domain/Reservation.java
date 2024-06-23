package com.example.petsitter.petsitter.domain;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.domain.MemberReservation;
import com.example.petsitter.petsitter.dto.ReservationDto;
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

    private String status;

    private Long totalPrice;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberReservation> members = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetsitterReservation> petsitters = new ArrayList<>();


    @Builder
    public Reservation(Long id, LocalDate date, List<LocalTime> times, String status, Long totalPrice , List<MemberReservation> members, List<PetsitterReservation> petsitters) {
        this.id = id;
        this.date = date;
        this.times = times;
        this.status = status;
        this.totalPrice = totalPrice;
        this.members = members;
        this.petsitters = petsitters;
    }

//    public void updateFromDTO(ReservationDto reservationDto) {
//        this.date = reservationDto.getDate();
//        this.times = reservationDto.getTimes();
//        this.status = reservationDto.getStatus();
//        this.totalPrice = reservationDto.getTotalPrice();
//    }
}
