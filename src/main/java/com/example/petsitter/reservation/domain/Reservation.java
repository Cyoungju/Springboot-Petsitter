package com.example.petsitter.reservation.domain;


import com.example.petsitter.board.domain.Comment;
import com.example.petsitter.member.domain.Member;
import com.example.petsitter.pet.domain.Pet;
import com.example.petsitter.petsitter.domain.PetsitterReservation;
import com.example.petsitter.wish.Wish;
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
@Table(name = "reservation",
        indexes = {
        @Index(name = "reservation_member_id_idx", columnList = "member_id")
}
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "reservation_times", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "reservation_time", nullable = false)
    private List<LocalTime> times = new ArrayList<>();

    @Column(nullable = false)
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<PetsitterReservation> petsitters = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;



    @Builder
    public Reservation(Long id, LocalDate date, List<LocalTime> times, Long totalPrice, ReservationStatus status, List<PetsitterReservation> petsitters, Member member) {
        this.id = id;
        this.date = date;
        this.times = times;
        this.totalPrice = totalPrice;
        this.status = status;
        this.petsitters = petsitters;
        this.member = member;
    }

    public void updateStatusDTO(String status) {
        if(status.equals("대기")) {
            this.status = ReservationStatus.대기;
        }else if(status.equals("확정")){
            this.status = ReservationStatus.확정;
        }else if(status.equals("취소")){
            this.status = ReservationStatus.취소;
        }
    }

}
