package com.example.petsitter.member.controller;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.pet.dto.PetDto;
import com.example.petsitter.pet.service.PetService;
import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.service.PetsitterService;
import com.example.petsitter.reservation.item.Item;
import com.example.petsitter.reservation.service.ReservationService;
import com.example.petsitter.wish.dto.WishDto;
import com.example.petsitter.wish.service.WishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/my")
public class MyController {

    private final MemberService memberService;
    private final PetService petService;
    private final PetsitterService petsitterService;
    private final ReservationService reservationService;
    private final WishService wishService;

    @GetMapping("/mypage")
    public String mypageP(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        String username = customUserDetails.getUsername();
        MemberDto memberDto = memberService.findByDtoEmail(username);

        model.addAttribute("member", memberDto);
        return "/my/mypage";
    }

    @GetMapping("/myWishList")
    public String myWishListP(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PageableDefault(page = 1) Pageable pageable, Model model){

        Page<WishDto> wishDtoPage = wishService.paging(pageable);

        int blockLimit = 3;
        //한 페이지에 보여질 페이지수
        int startPage = (int)(Math.ceil((double)pageable.getPageNumber() / blockLimit) - 1) * blockLimit + 1;
        // 현재 페이지 블록의 시작 페이지를 계산
        int endPage = ((startPage + blockLimit - 1) < wishDtoPage.getTotalPages()) ? (startPage + blockLimit - 1) : wishDtoPage.getTotalPages();
        // 현재 페이지의 블록의 끝페이지를 계산



        model.addAttribute("list", wishDtoPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/my/myWishList";
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
        return "/my/myPetsitterList";
    }

    @GetMapping("/myPetsitterResList")
    public String PetsitterResListP(Model model,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUsername();
        Member member = memberService.findByEmail(username);

        List<Item> item = reservationService.findAllItemsByMember(member);

        model.addAttribute("list", item);
        return "/my/myPetsitterResList";
    }

    @GetMapping("/myPetsitterItemResList")
    public String myPetsitterItemResListP(Model model,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUsername();
        Member member = memberService.findByEmail(username);

        List<Item> item = reservationService.findPetsitterReservation(member);

        //pet정보
        List<PetDto> petDto = petService.getList();

        model.addAttribute("list", item);
        model.addAttribute("pet", petDto);
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


    @GetMapping("/petReservation/info")
    @ResponseBody
    public List<PetDto> getPetInfo(@RequestParam("reservationId") Long reservationId) {
        List<PetDto> petDtoList = petService.findPetsByReservationId(reservationId);
        return petDtoList;
    }

}
