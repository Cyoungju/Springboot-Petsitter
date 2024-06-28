package com.example.petsitter.reservation.service;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.reservation.dto.ReservationDto;
import com.example.petsitter.reservation.item.Item;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Transactional
public interface ReservationService {

    List<LocalTime> getTimesByDateAndPetsitter(LocalDate date, Long petsitterId);

    void addReservation(String username, ReservationDto reservationDto);

    Long calculateTotalPriceByTime(Long petsitterId, List<LocalTime> time);

    List<Item> findAllItemsByMember(Member member);

    void updateStatus(String status, Long id);

    List<Item> findPetsitterReservation(Member member);

 

}
