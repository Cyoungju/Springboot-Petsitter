package com.example.petsitter.pet.dto;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.pet.domain.Pet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String petName;

    @NotNull(message = "타입은 값을 반드시 입력해야합니다.")
    private boolean petType;

    private boolean petGender;

    private LocalDate petBirth;

    private String petBreed;

    private boolean petNeutering;

    private float petWeight;


    // 데이터베이스에 파일을 저장하면안됨
    // 등록
    // 조회

    // 업로드시 사용
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    //이미 업로드 되어있는 파일들의 이름
    @Builder.Default
    private List<String> uploadedFileName = new ArrayList<>();

    public Pet toEntity(Member member) {
        Pet pet = Pet.builder()
                .petName(petName)
                .petType(petType)
                .petGender(petGender)
                .petBirth(petBirth)
                .petBreed(petBreed)
                .petNeutering(petNeutering)
                .petWeight(petWeight)
                .member(member)
                .build();

        if(uploadedFileName == null){
            return pet;
        }

        uploadedFileName.stream().forEach(uploadName -> {
            pet.addImageString(uploadName);
        });
        return pet;
    }


}
