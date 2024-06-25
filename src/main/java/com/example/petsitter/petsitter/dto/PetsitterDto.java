package com.example.petsitter.petsitter.dto;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.petsitter.domain.Petsitter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetsitterDto {

    private Long id;

    @NotBlank(message = "필수 입력 값입니다.")
    private String sitterName;


    private String sitterContent;

    @NotNull(message = "필수 입력 값입니다.")
    private boolean sitterType;

    private Long sitterPrice;

    @NotBlank(message = "필수 입력 값입니다.")
    private String sitterWorkAdr;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long memberId;


    // 업로드시 사용
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    //이미 업로드 되어있는 파일들의 이름
    @Builder.Default
    private List<String> uploadedFileName = new ArrayList<>();

    public PetsitterDto(Long id, String sitterName, String sitterContent, boolean sitterType, Long sitterPrice, String sitterWorkAdr, LocalDateTime createTime, LocalDateTime updateTime, List<String> uploadedFileName) {
        this.id = id;
        this.sitterName = sitterName;
        this.sitterContent = sitterContent;
        this.sitterType = sitterType;
        this.sitterPrice = sitterPrice;
        this.sitterWorkAdr = sitterWorkAdr;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.uploadedFileName = uploadedFileName;
    }

    public static PetsitterDto topetsitterDTO(Petsitter petsitter) {
        return new PetsitterDto(
                petsitter.getId(),
                petsitter.getSitterName(),
                petsitter.getSitterContent(),
                petsitter.isSitterType(),
                petsitter.getSitterPrice(),
                petsitter.getSitterWorkAdr(),
                petsitter.getCreateTime(),
                petsitter.getUpdateTime(),
                petsitter.getImageList().stream().map(
                        images-> images.getFileName()
                ).collect(Collectors.toList())
        );
    }


    public Petsitter toEntity(Member member){
        Petsitter petsitter = Petsitter.builder()
                .id(id)
                .sitterName(sitterName)
                .sitterContent(sitterContent)
                .sitterType(sitterType)
                .sitterPrice(sitterPrice)
                .sitterWorkAdr(sitterWorkAdr)
                .createTime(createTime)
                .updateTime(LocalDateTime.now())
                .member(member)
                .build();

        if(uploadedFileName == null){
            return petsitter;
        }

        uploadedFileName.stream().forEach(uploadName -> {
            petsitter.addImageString(uploadName);
        });

        return petsitter;
    }

}
