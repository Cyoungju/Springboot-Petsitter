package com.example.petsitter.member.controller;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.domain.MemberRole;
import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.pet.dto.PetDto;
import com.example.petsitter.pet.service.PetService;
import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.service.PetsitterService;
import com.example.petsitter.reservation.domain.Reservation;
import com.example.petsitter.reservation.dto.ReservationDto;
import com.example.petsitter.reservation.item.Item;
import com.example.petsitter.reservation.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/my")
public class MyController {

    private final MemberService memberService;
    private final PetService petService;
    private final PetsitterService petsitterService;
    private final ReservationService reservationService;

    @GetMapping("/mypage")
    public String mypageP(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        String username = customUserDetails.getUsername();
        MemberDto memberDto = memberService.findByDtoEmail(username);

        model.addAttribute("member", memberDto);
        return "/my/mypage";
    }

    @GetMapping("/myPetlist")
    public String petlistP(Model model){
        List<PetDto> petDtoList = petService.getList();
        model.addAttribute("list", petDtoList);
        return "/my/myPetlist";
    }

    @GetMapping("/myPetsitterList")
    public String petsitterP(Model model){
        List<PetsitterDto> petsitterDtoList = petsitterService.getList();
        model.addAttribute("list", petsitterDtoList);
        return "/my/petsitterList";
    }

    @GetMapping("/myPetsitterResList")
    public String PetsitterResListP(Model model,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUsername();
        Member member = memberService.findByEmail(username);

        List<Item> item = reservationService.findAllItemsByMember(member);

        model.addAttribute("list", item);
        return "/my/petsitterResList";
    }

    @GetMapping("/myPetsitterItemResList")
    public String myPetsitterItemResListP(Model model,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUsername();
        Member member = memberService.findByEmail(username);

        List<Item> item = reservationService.findPetsitterReservation(member);


        model.addAttribute("list", item);
        return "/my/myPetsitterItemResList";
    }


    @PostMapping("/sitterRole")
    public ResponseEntity<String> sitterRole(@Valid @RequestBody MemberDto memberDto){
        memberService.addRole();
        return ResponseEntity.ok("success");
    }
    @PostMapping("/userRole")
    public ResponseEntity<String> userRole(@Valid @RequestBody MemberDto memberDto){
        memberService.clearRole();
        return ResponseEntity.ok("success");
    }


}
