package com.example.petsitter.pet.domain;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.pet.dto.PetDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "pet")
@ToString(exclude = "imageList")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String petName;

    @Column(nullable = false)
    private boolean petType;

    private boolean petGender;

    private LocalDate petBirth;

    @Column(length = 50)
    private String petBreed;

    private boolean petNeutering;

    private float petWeight;

    @ElementCollection
    @Builder.Default
    private List<PetImage> imageList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public void addImage(PetImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName){

        PetImage petImage = PetImage.builder()
                .fileName(fileName)
                .build();
        addImage(petImage);

    }
    public void clearList() {
        this.imageList.clear();
    }

    @Builder
    public Pet(Long id, String petName, boolean petType, boolean petGender, LocalDate petBirth, String petBreed, boolean petNeutering, float petWeight, List<PetImage> imageList, Member member) {
        this.id = id;
        this.petName = petName;
        this.petType = petType;
        this.petGender = petGender;
        this.petBirth = petBirth;
        this.petBreed = petBreed;
        this.petNeutering = petNeutering;
        this.petWeight = petWeight;
        this.imageList = imageList;
        this.member = member;
    }

    public void updateFromDTO(PetDto petDto){
        // 모든 변경 사항을 셋팅. =>  기존에 있는 데이터에 저장해야하기 때문에 new 객체 생성을 하는 toEntity 사용 불가
        this.petName = petDto.getPetName();
        this.petType = petDto.isPetType();
        this.petGender = petDto.isPetGender();
        this.petBirth = petDto.getPetBirth();
        this.petBreed = petDto.getPetBreed();
        this.petNeutering = petDto.isPetNeutering();
        this.petWeight = petDto.getPetWeight();
    }
}
