package com.example.petsitter.petsitter.controller;


import com.example.petsitter.pet.dto.PetDto;
import com.example.petsitter.petsitter.domain.Reservation;
import com.example.petsitter.petsitter.domain.ReservationTime;
import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.dto.ReservationDto;
import com.example.petsitter.petsitter.service.PetsitterService;
import com.example.petsitter.core.util.CustomFileUtil;
import com.example.petsitter.petsitter.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/petsitter")
public class PetsitterController {

    private final PetsitterService petsitterService;
    private final CustomFileUtil fileUtil;

    @GetMapping("/sitterRole/create")
    public String create(Model model){
        model.addAttribute("petsitterDto", new PetsitterDto());
        return "/petsitter/create";
    }

    @PostMapping("/")
    public String resister(@Valid PetsitterDto petsitterDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/petsitter/create";
        }

        petsitterService.save(petsitterDto);
        return "redirect:/petsitter/list";
    }


    @GetMapping("/list")
    public String getList(@PageableDefault(page = 1) Pageable pageable, String searchKeyword, Model model){
        Page<PetsitterDto> petsitters = null;


        if(searchKeyword == null){
            petsitters = petsitterService.paging(pageable);
        }else if(searchKeyword != ""){
            petsitters = petsitterService.pagingSearchList(searchKeyword, pageable);
        }

        int blockLimit = 3;
        //한 페이지에 보여질 페이지수
        int startPage = (int)(Math.ceil((double)pageable.getPageNumber() / blockLimit) - 1) * blockLimit + 1;
        // 현재 페이지 블록의 시작 페이지를 계산
        int endPage = ((startPage + blockLimit - 1) < petsitters.getTotalPages()) ? (startPage + blockLimit - 1) : petsitters.getTotalPages();
        // 현재 페이지의 블록의 끝페이지를 계산


        model.addAttribute("petsitterList", petsitters);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/petsitter/list";
    }

    //하나만 불러오게 - 상세페이지
    @GetMapping("/{id}")
    public String paging(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable){
        PetsitterDto petsitterDto = petsitterService.findById(id);

        //모델에 데이터 추가
        model.addAttribute("petsitter", petsitterDto);
        model.addAttribute("page", pageable.getPageNumber());

        return "/petsitter/detail";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        PetsitterDto petsitterDto = petsitterService.findById(id);
        model.addAttribute("petsitterDto", petsitterDto);
        return "/petsitter/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("petsitterDto") PetsitterDto petsitterDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/petsitter/update/"+ petsitterDto.getId();
        }
        petsitterService.update(petsitterDto);
        // 회원 정보 업데이트 로직
        return "redirect:/my/myPetsitterList";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        petsitterService.delete(id);
        return ResponseEntity.ok("SUCCESS");

    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        return fileUtil.getFile(fileName);
    }


    // 에약하기 페이지
    @GetMapping("/reservation/{id}")
    public String reserv(@PathVariable Long id, Model model) {

        // ReservationDto 객체를 생성하여 모델에 추가
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setPetsitterId(id);
        model.addAttribute("reservationDto", reservationDto);

        List<LocalTime> reservationTimeList = ReservationTime.getReservationTime();
        model.addAttribute("reservationTimeList", reservationTimeList);

        return "/petsitter/reservation";
    }
}
