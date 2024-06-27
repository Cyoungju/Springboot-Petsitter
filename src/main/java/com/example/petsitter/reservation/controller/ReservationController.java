package com.example.petsitter.reservation.controller;


import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.reservation.dto.ReservationDto;
import com.example.petsitter.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 상세 내역
    @GetMapping("/check")
    public String reservationCheck(){

        return "/petsitter/check";
    }



    @PostMapping("/")
    public String submitReservation(@Valid @ModelAttribute("reservationDto") ReservationDto reservationDto,
                                    @ModelAttribute("date") LocalDate date,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();

        reservationService.addReservation(username, reservationDto);

        return "redirect:/reservation/check";
    }

    @GetMapping("/getReservationTimes")
    public @ResponseBody List<String> getReservationTimes(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                          @RequestParam("petsitterId") Long petsitterId) {
        List<LocalTime> reservationTimes = reservationService.getTimesByDateAndPetsitter(date, petsitterId);
        return reservationTimes.stream()
                .map(time -> time.format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());
    }


    @GetMapping("/getTotalPriceByTime")
    public @ResponseBody Long getTotalPriceByTime(
            @RequestParam("petsitterId") Long petsitterId,
            @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) List<LocalTime> time) {

        // totalPrice 계산 로직
        Long totalPrice = reservationService.calculateTotalPriceByTime(petsitterId, time);

        return totalPrice;
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestParam String status, @RequestParam Long id) {
        System.out.println(status);
        reservationService.updateStatus(status, id);
        return ResponseEntity.ok("SUCCESS");
    }


}
