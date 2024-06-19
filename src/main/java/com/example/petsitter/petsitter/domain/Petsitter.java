package com.example.petsitter.petsitter.domain;

import com.example.petsitter.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Builder
@Entity
public class Petsitter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sitterName;

    private String sitterContent;

    private boolean sitterType;

    private Long sitterPrice;

    private String sitterWorkAdr;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @ElementCollection
    @Builder.Default
    private List<PetsitterImage> imageList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void addImage(PetsitterImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName){

        PetsitterImage petsitterImage = PetsitterImage.builder()
                .fileName(fileName)
                .build();
        addImage(petsitterImage);
    }
    public void clearList() {
        this.imageList.clear();
    }

    @Builder
    public Petsitter(Long id, String sitterName, String sitterContent, boolean sitterType, Long sitterPrice, String sitterWorkAdr, LocalDateTime createTime, LocalDateTime updateTime, List<PetsitterImage> imageList, Member member) {
        this.id = id;
        this.sitterName = sitterName;
        this.sitterContent = sitterContent;
        this.sitterType = sitterType;
        this.sitterPrice = sitterPrice;
        this.sitterWorkAdr = sitterWorkAdr;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.imageList = imageList;
        this.member = member;
    }
}
