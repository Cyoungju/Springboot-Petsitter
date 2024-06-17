package com.example.petsitter.pet.service;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.pet.domain.Pet;
import com.example.petsitter.pet.dto.PetDto;
import com.example.petsitter.pet.repository.PetRepository;
import com.example.petsitter.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class PetServiceImpl implements PetService{

    private final CustomFileUtil fileUtil;

    private final PetRepository petRepository;

    private final MemberService memberService;

    @Override
    public void save(PetDto petDto) {
        //파일 저장
        log.info("register : "+ petDto);
        List<MultipartFile> files = petDto.getFiles();

        // 파일이 없는 경우 처리
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일을 선택해주세요.");
        }

        List<String> uploadedFileNames = fileUtil.saveFiles(files);
        petDto.setUploadedFileName(uploadedFileNames);
        log.info("uploadedFileNames : "+uploadedFileNames);

        String username = memberService.getAuthName();
        Member member = memberService.findByEmail(username);

        petRepository.save(petDto.toEntity(member));
    }

    @Override
    public List<PetDto> getList() {
        String username = memberService.getAuthName();
        Long id = memberService.findByEmail(username).getId();

        List<Pet> pets = petRepository.findByMemberId(id);

        List<PetDto> petDtos = pets.stream().map(
                pet -> {
                    String firstImage = pet.getImageList().isEmpty() ? null : pet.getImageList().get(0).getFileName();
                    return PetDto.builder()
                            .petName(pet.getPetName())
                            .petType(pet.isPetType())
                            .petGender(pet.isPetGender())
                            .petBirth(pet.getPetBirth())
                            .petBreed(pet.getPetBreed())
                            .petNeutering(pet.isPetNeutering())
                            .petWeight(pet.getPetWeight())
                            .uploadedFileName(Collections.singletonList(firstImage)) // 첫 번째 이미지만 리스트로 설정
                            .build();
                }
        ).collect(Collectors.toList());

        return petDtos;
    }

}
