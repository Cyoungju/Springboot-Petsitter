package com.example.petsitter.member.domain;

import com.example.petsitter.petsitter.domain.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "member_reservation")
public class MemberReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public MemberReservation(Member member, Reservation reservation) {
        this.member = member;
        this.reservation = reservation;
    }
}
