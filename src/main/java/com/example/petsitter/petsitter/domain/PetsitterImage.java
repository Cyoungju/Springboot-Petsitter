package com.example.petsitter.petsitter.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "petsitterImage")
public class PetsitterImage {
    private String fileName;

    private int ord;

    public void setOrd(int ord){
        this.ord = ord;
    }
}
