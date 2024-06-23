package com.example.petsitter.petsitter.controller;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.domain.Reservation;
import com.example.petsitter.petsitter.domain.ReservationTime;
import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.dto.ReservationDto;
import com.example.petsitter.petsitter.repository.PetsitterRepository;
import com.example.petsitter.petsitter.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;



    @PostMapping("/")
    public String submitReservation(@Valid @ModelAttribute("reservationDto") ReservationDto reservationDto,
                                    @ModelAttribute("date") LocalDate date) {
        //reservationService.save(reservationDto);

        return "redirect:/"; // 예약 후에는 다시 예약 페이지로 리다이렉트
    }

    @GetMapping("/getReservationTimes")
    public @ResponseBody List<String> getReservationTimes(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                          @RequestParam("petsitterId") Long petsitterId) {
        List<LocalTime> reservationTimes = reservationService.getTimesByDateAndPetsitter(date, petsitterId);
        return reservationTimes.stream()
                .map(time -> time.format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());
    }
//
//    @GetMapping("/getTotalPriceByTime")
//    public @ResponseBody Long getTotalPriceByTime(
//            @RequestParam("petsitterId") Long petsitterId,
//            @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) List<LocalTime> time) {
//
//        // totalPrice 계산 로직
//        Long totalPrice = reservationService.calculateTotalPriceByTime(petsitterId, time);
//
//        return totalPrice;
//    }


}
