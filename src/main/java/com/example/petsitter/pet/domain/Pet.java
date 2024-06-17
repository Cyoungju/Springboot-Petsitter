package com.example.petsitter.pet.domain;


import com.example.petsitter.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Entity
@Builder
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void addImage(PetImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName){

        PetImage productImage = PetImage.builder()
                .fileName(fileName)
                .build();
        addImage(productImage);

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
}
