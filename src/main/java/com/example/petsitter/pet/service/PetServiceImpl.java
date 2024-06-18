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
import java.util.Optional;
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
                            .id(pet.getId())
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

    @Override
    public void update(PetDto petDto) {
        Optional<Pet> petOptional = petRepository.findById(petDto.getId());

        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            pet.updateFromDTO(petDto);

            // 새로 업로드할 파일
            List<MultipartFile> files = petDto.getFiles();
            List<String> uploadedFileName = null;
            // 파일 리스트 로그 출력
            log.info("받은 파일들: " + (files.isEmpty() ? "null" : files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.toList())));


            // 만약 기존에 등록된 이미지가 없는 경우
            if(pet.getImageList().isEmpty()) {
                log.info("기존에 등록된 파일 없음" );
                if (files.isEmpty()) {
                    log.info("파일등록 안함");
                }else{
                    log.info("파일등록");
                    uploadedFileName = fileUtil.saveFiles(files);
                    petDto.setUploadedFileName(uploadedFileName);

                    for (String fileName : uploadedFileName) {
                        pet.addImageString(fileName);
                    }
                }
            }else{
                log.info("기존에 등록된 파일 있음");
                if (files.isEmpty()) {
                    // 파일이 없음
                    uploadedFileName = pet.getImageList().stream()
                            .map(image -> image.getFileName())
                            .collect(Collectors.toList());
                    petDto.setUploadedFileName(uploadedFileName);
                    log.info("Uploaded file names: " + uploadedFileName);
                }else{
                    // 기존의 등록된 파일을 삭제
                    fileUtil.deleteFiles(pet.getImageList().stream()
                            .map(image -> image.getFileName())
                            .collect(Collectors.toList())
                    );

                    uploadedFileName = fileUtil.saveFiles(files);
                    petDto.setUploadedFileName(uploadedFileName);

                    pet.getImageList().clear();
                    for (String fileName : uploadedFileName) {
                        pet.addImageString(fileName);
                    }

                }
            }

            petRepository.save(pet);
        }
    }

    @Override
    public void delete(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    public PetDto findById(Long id) {
        //Optional존재유무 확인
        Optional<Pet> boardOptional = petRepository.findById(id);

        // Optional.isPresent()를 사용하여 값이 있는지 확인
        if (boardOptional.isPresent()) {
            Pet pet = petRepository.findById(id).get();

            //toEntity에서 DTO로 데이터를 변환
            return PetDto.builder()
                    .id(pet.getId())
                    .petName(pet.getPetName())
                    .petType(pet.isPetType())
                    .petGender(pet.isPetGender())
                    .petBirth(pet.getPetBirth())
                    .petBreed(pet.getPetBreed())
                    .petNeutering(pet.isPetNeutering())
                    .petWeight(pet.getPetWeight())
                    .build();
        }else {
            return null;
        }
    }


}
