package com.example.petsitter.petsitter.domain;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.petsitter.dto.PetsitterDto;
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
@Table(name = "petsitter")
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

    @OneToMany(mappedBy = "petsitter", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PetsitterReservation> reservations = new ArrayList<>();

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
    public Petsitter(Long id, String sitterName, String sitterContent, boolean sitterType, Long sitterPrice, String sitterWorkAdr, LocalDateTime createTime, LocalDateTime updateTime, List<PetsitterImage> imageList,List<PetsitterReservation> reservations, Member member) {
        this.id = id;
        this.sitterName = sitterName;
        this.sitterContent = sitterContent;
        this.sitterType = sitterType;
        this.sitterPrice = sitterPrice;
        this.sitterWorkAdr = sitterWorkAdr;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.imageList = imageList;
        this.reservations = reservations;
        this.member = member;
    }

    public void updateFromDTO(PetsitterDto petsitterDto){
        // 모든 변경 사항을 셋팅. =>  기존에 있는 데이터에 저장해야하기 때문에 new 객체 생성을 하는 toEntity 사용 불가
        this.sitterName = petsitterDto.getSitterName();
        this.sitterContent = petsitterDto.getSitterContent();
        this.sitterType = petsitterDto.isSitterType();
        this.sitterPrice = petsitterDto.getSitterPrice();
        this.sitterWorkAdr = petsitterDto.getSitterWorkAdr();
        this.updateTime = petsitterDto.getUpdateTime();
    }
}
