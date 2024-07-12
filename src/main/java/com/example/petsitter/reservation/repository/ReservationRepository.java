package com.example.petsitter.reservation.repository;

import com.example.petsitter.reservation.domain.Reservation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Cacheable(value = "reservationTimes")
    @Query("SELECT r.times FROM Reservation r JOIN r.petsitters pr WHERE r.date = :date AND pr.petsitter.id = :petsitterId AND r.status IN ('대기', '확정')")
    List<LocalTime> findTimesByDateAndNotConfirmed(@Param("date") LocalDate date, @Param("petsitterId") Long petsitterId);

    List<Reservation> findByPetsitters_PetsitterId(Long id);
}
