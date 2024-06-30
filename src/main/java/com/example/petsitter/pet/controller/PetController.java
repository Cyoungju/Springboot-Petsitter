package com.example.petsitter.pet.controller;


import com.example.petsitter.pet.dto.PetDto;
import com.example.petsitter.pet.service.PetService;
import com.example.petsitter.core.util.CustomFileUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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


    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        PetDto petDto = petService.findById(id);
        model.addAttribute("petDto", petDto);
        return "/pet/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("petDto") PetDto petDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/pet/update/"+ petDto.getId();
        }
        petService.update(petDto);
        // 회원 정보 업데이트 로직
        return "redirect:/my/myPetlist";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.ok("success");
    }

}
