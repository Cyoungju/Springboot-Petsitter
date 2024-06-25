package com.example.petsitter.reservation.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public abstract class ReservationTime {
    public static List<LocalTime> getReservationTime() {
        List<LocalTime> times = new ArrayList<>();

        times.add(LocalTime.of(12,0,0));
        times.add(LocalTime.of(1,0,0));
        times.add(LocalTime.of(2,0,0));
        times.add(LocalTime.of(3,0,0));
        times.add(LocalTime.of(4,0,0));
        times.add(LocalTime.of(5,0,0));
        times.add(LocalTime.of(6,0,0));
        times.add(LocalTime.of(7,0,0));
        times.add(LocalTime.of(8,0,0));
        times.add(LocalTime.of(9,0,0));
        times.add(LocalTime.of(10,0,0));
        times.add(LocalTime.of(11,0,0));
        times.add(LocalTime.of(12,0,0));
        times.add(LocalTime.of(13,0,0));
        times.add(LocalTime.of(14,0,0));
        times.add(LocalTime.of(15,0,0));
        times.add(LocalTime.of(16,0,0));
        times.add(LocalTime.of(17,0,0));
        times.add(LocalTime.of(18,0,0));
        times.add(LocalTime.of(19,0,0));
        times.add(LocalTime.of(20,0,0));
        times.add(LocalTime.of(21,0,0));
        times.add(LocalTime.of(22,0,0));
        times.add(LocalTime.of(23,0,0));

        return times;
    }
}
