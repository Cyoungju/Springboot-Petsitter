package com.example.petsitter.petsitter.controller;


import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.service.PetsitterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/petsitter")
public class PetsitterController {

    private final PetsitterService petsitterService;

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

}
