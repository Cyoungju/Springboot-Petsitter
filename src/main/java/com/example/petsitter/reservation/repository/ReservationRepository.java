package com.example.petsitter.reservation.repository;

import com.example.petsitter.reservation.domain.Reservation;
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

    @Query("SELECT r FROM Reservation r JOIN r.petsitters pr WHERE r.date = :date AND pr.petsitter.id = :petsitterId")
    Optional<Reservation> findByDateAndPetsitter(LocalDate date, Long petsitterId);
}
