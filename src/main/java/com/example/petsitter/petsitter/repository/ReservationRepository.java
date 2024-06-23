package com.example.petsitter.petsitter.repository;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.domain.Reservation;
import com.example.petsitter.petsitter.dto.ReservationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    @Query("SELECT r.times FROM Reservation r JOIN r.petsitters pr WHERE r.date = :date AND pr.petsitter.id = :petsitterId")
    List<LocalTime> findTimesByDateAndPetsitter(@Param("date") LocalDate date, @Param("petsitterId") Long petsitterId);


//    List<Reservation> findByDate(LocalDate date);
//
//
//    Optional<Reservation> findByDateAndPetsitter(LocalDate date, Petsitter petsitter);
//
//
//    List<LocalTime> findTimesByDateAndPetsitters(@Param("date") LocalDate date, @Param("petsitterId") Long petsitterId);
//
//    Optional<Reservation> findByDateAndPetsitters(LocalDate date, Petsitter petsitter);
}
