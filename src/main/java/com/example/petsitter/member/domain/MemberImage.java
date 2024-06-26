package com.example.petsitter.member.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "memberImage")
public class MemberImage {
    private String fileName;

    private int ord;

    public void setOrd(int ord){
        this.ord = ord;
    }
}
