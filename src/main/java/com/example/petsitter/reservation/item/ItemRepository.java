package com.example.petsitter.reservation.item;

import com.example.petsitter.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByReservationMember(Member member);

    List<Item> findAllByReservation_Petsitters_Petsitter_Member(Member member);
}
