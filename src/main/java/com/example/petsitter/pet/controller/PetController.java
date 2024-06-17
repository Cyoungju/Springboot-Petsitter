package com.example.petsitter.pet.controller;


import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.pet.dto.PetDto;
import com.example.petsitter.pet.service.PetService;
import com.example.petsitter.util.CustomFileUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/pet")
public class PetController {

    private final CustomFileUtil fileUtil;
    private final PetService petService;

    @PostMapping("/")
    public String resister(@Valid PetDto petDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/pet/create";
        }

        petService.save(petDto);
        return "redirect:/my/myPetlist";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("petDto", new PetDto());
        return "/pet/create";
    }


    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        return fileUtil.getFile(fileName);
    }


}
