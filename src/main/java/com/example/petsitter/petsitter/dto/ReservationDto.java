package com.example.petsitter.petsitter.dto;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.domain.Reservation;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private String status;

    private Long totalPrice;

    private Long petsitterId;

    private List<Long> petsitterIds; // Petsitter ID 리스트

    private Long memberId;

    private List<Long> memberIds; // Member ID 리스트


}
