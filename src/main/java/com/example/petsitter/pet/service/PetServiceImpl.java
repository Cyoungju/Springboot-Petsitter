package com.example.petsitter.pet.service;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.pet.domain.Pet;
import com.example.petsitter.pet.dto.PetDto;
import com.example.petsitter.pet.repository.PetRepository;
import com.example.petsitter.core.util.CustomFileUtil;
import com.example.petsitter.reservation.domain.Reservation;
import com.example.petsitter.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
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
    private final ReservationRepository reservationRepository;

    @Override
    public void save(PetDto petDto) {
        //파일 저장
        log.info("register : "+ petDto);
        List<MultipartFile> files = petDto.getFiles();

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

        PetDto oldPetDto = findById(petDto.getId());

        Pet pet = petOptional.get();

        //기존의 파일들 (데이터베이스에 존재하는 파일들)
        List<String> oldFileNames = oldPetDto.getUploadedFileName();
        System.out.println("기존의 파일들 - oldFileNames : "+oldFileNames);

        //새로 업로드할 파일들
        List<MultipartFile> files = petDto.getFiles();
        System.out.println("새로 업로드할 파일들 - files : "+files);

        //새로 업로드 해야하는 파일들
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);
        System.out.println("새로 업로드 해야하는 파일들 - currentUploadFileNames : "+currentUploadFileNames);

        // 화면에서 변화 없이 유지 되어야할 파일들
        List<String> uploadedFileNames = petDto.getUploadedFileName();
        System.out.println("화면에서 변화 없이 유지 되어야할 파일들 - uploadedFileNames : "+uploadedFileNames);

        //유지되는 파일들  + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이 됨
        if(currentUploadFileNames != null && currentUploadFileNames.size() > 0) {

            uploadedFileNames.addAll(currentUploadFileNames);

        }

        pet.updateFromDTO(petDto);
        pet.clearList();
        List<String> uploadFileNames = petDto.getUploadedFileName();

        if(uploadFileNames != null && uploadFileNames.size() > 0 ){
            uploadFileNames.stream().forEach(uploadName -> {
                pet.addImageString(uploadName);
            });
        }

        petRepository.save(pet);


        if(oldFileNames != null && oldFileNames.size() > 0){
            // 지워져야하는 파일 목록 찾기
            // 예전 파일들 중에 지워져야하는 파일 이름들
            List<String> removeFiles = oldFileNames
                    .stream()
                    .filter(fileNeme -> uploadedFileNames.indexOf(fileNeme) == -1).collect(Collectors.toList());

            // 실제 파일 삭제
            fileUtil.deleteFiles(removeFiles);
        }

    }

    @Override
    public void delete(Long id) {
        PetDto petDto = findById(id);
        List<String> oldFileNames = petDto.getUploadedFileName();
        petRepository.deleteById(id);

        fileUtil.deleteFiles(oldFileNames);
    }

    @Override
    public PetDto findById(Long id) {
        //Optional존재유무 확인
        Optional<Pet> petOptional = petRepository.findById(id);

        // Optional.isPresent()를 사용하여 값이 있는지 확인
        if (petOptional.isPresent()) {
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
                    .uploadedFileName(pet.getImageList().stream().map(
                            image -> image.getFileName()
                    ).collect(Collectors.toList()))
                    .build();
        }else {
            return null;
        }
    }

    @Override
    public List<PetDto> findPetsByReservationId(Long reservationId) {
        Optional<Reservation> reservationOption = reservationRepository.findById(reservationId);
        if(reservationOption.isPresent()){
            Reservation reservation = reservationOption.get();
            Long member = reservation.getMember().getId();
            List<Pet> pets = petRepository.findByMemberId(member);


            // Convert pets to DTOs
            List<PetDto> petDtos = pets.stream().map(pet -> {
                String firstImage = pet.getImageList().isEmpty() ? null : pet.getImageList().get(0).getFileName();
                return PetDto.builder()
                        .id(pet.getId())
                        .petName(pet.getPetName())
                        .petType(pet.isPetType())
                        .petGender(pet.isPetGender())
                        .petBirth(pet.getPetBirth())
                        .petNeutering(pet.isPetNeutering())
                        .petWeight(pet.getPetWeight())
                        .uploadedFileName(pet.getImageList().stream().map(
                                image -> image.getFileName()
                        ).collect(Collectors.toList()))
                        .build();
            }).collect(Collectors.toList());

            return petDtos;
        }else {
            return null;
        }



    }


}
