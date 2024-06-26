package com.example.petsitter.pet.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "petImage")
public class PetImage {

    private String fileName;

    private int ord;

    public void setOrd(int ord){
        this.ord = ord;
    }
}
