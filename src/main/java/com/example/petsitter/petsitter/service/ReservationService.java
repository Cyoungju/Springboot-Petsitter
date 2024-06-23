package com.example.petsitter.petsitter.service;


import com.example.petsitter.petsitter.domain.Reservation;
import com.example.petsitter.petsitter.dto.ReservationDto;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Transactional
public interface ReservationService {
//    void save(ReservationDto reservationDto);
//
    List<LocalTime> getTimesByDateAndPetsitter(LocalDate date, Long petsitterId);
//
//    Long calculateTotalPriceByTime(Long petsitterId, List<LocalTime> time);
}
