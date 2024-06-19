package com.example.petsitter.petsitter.service;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.repository.PetsitterRepository;
import com.example.petsitter.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class PetsitterServiceImpl implements PetsitterService {

    private final CustomFileUtil fileUtil;

    private final MemberService memberService;

    private final PetsitterRepository petsitterRepository;

    @Override
    public void save(PetsitterDto petsitterDto) {
        List<MultipartFile> files = petsitterDto.getFiles();

        List<String> uploadedFileNames = fileUtil.saveFiles(files);
        petsitterDto.setUploadedFileName(uploadedFileNames);

        //사용자 받아오기
        String uusername = memberService.getAuthName();
        Member member = memberService.findByEmail(uusername);

        petsitterRepository.save(petsitterDto.toEntity(member));
    }

    @Override
    public Page<PetsitterDto> paging(Pageable pageable) {
        int page = pageable.getPageNumber() -1;
        int size = 5;

        Page<Petsitter> petsitterDtos = petsitterRepository.findAll(
                PageRequest.of(page,size)
        );

        return petsitterDtos.map(
                petsitter -> {
                    return PetsitterDto.builder()
                            .id(petsitter.getId())
                            .sitterName(petsitter.getSitterName())
                            .sitterContent(petsitter.getSitterContent())
                            .sitterType(petsitter.isSitterType())
                            .sitterPrice(petsitter.getSitterPrice())
                            .sitterWorkAdr(petsitter.getSitterWorkAdr())
                            .createTime(petsitter.getCreateTime())
                            .updateTime(petsitter.getUpdateTime())
                            .build();
                }
        );
    }

    @Override
    public Page<PetsitterDto> pagingSearchList(String searchKeyword, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int size = 5;

        Page<Petsitter> petsitterDtos = petsitterRepository.findBySitterWorkAdrContainingIgnoreCase(
                searchKeyword, PageRequest.of(page,size)
        );
        return petsitterDtos.map(
                petsitter -> {
                    return PetsitterDto.builder()
                            .sitterName(petsitter.getSitterName())
                            .sitterContent(petsitter.getSitterContent())
                            .sitterType(petsitter.isSitterType())
                            .sitterPrice(petsitter.getSitterPrice())
                            .sitterWorkAdr(petsitter.getSitterWorkAdr())
                            .createTime(petsitter.getCreateTime())
                            .updateTime(petsitter.getUpdateTime())
                            .build();
                }
        );
    }


}
