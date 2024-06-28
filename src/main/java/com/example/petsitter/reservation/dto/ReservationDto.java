package com.example.petsitter.reservation.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Long id;

    @NotNull(message = "날짜를 선택해주세요.")
    private LocalDate date;

    @NotNull(message = "시간을 선택해주세요.")
    @Builder.Default
    private List<LocalTime> times = new ArrayList<>();

    private Long totalPrice;

    private String status;

    private Long petsitterId; 

}
