package com.example.petsitter.petsitter.service;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.repository.PetsitterRepository;
import com.example.petsitter.core.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class PetsitterServiceImpl implements PetsitterService {

    private final CustomFileUtil fileUtil;

    private final MemberService memberService;

    private final PetsitterRepository petsitterRepository;

    @Override
    public void save(PetsitterDto petsitterDto) {
        //게시물 현재시간 저장
        petsitterDto.setCreateTime(LocalDateTime.now());
        List<MultipartFile> files = petsitterDto.getFiles();

        List<String> uploadedFileNames = fileUtil.saveSlideFiles(files);
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
                PageRequest.of(page,size, Sort.by("id").descending())
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
                searchKeyword, PageRequest.of(page,size, Sort.by("id").descending())
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

    @Override
    public PetsitterDto findById(Long id) {
        //Optional존재유무 확인
        Optional<Petsitter> boardOptional = petsitterRepository.findById(id);

        // Optional.isPresent()를 사용하여 값이 있는지 확인
        if (boardOptional.isPresent()) {
            Petsitter petsitter = petsitterRepository.findById(id).get();
            //toEntity에서 DTO로 데이터를 변환
            return PetsitterDto.topetsitterDTO(petsitter);
        }else {
            return null;
        }
    }



    @Override
    public void update(PetsitterDto petsitterDto) {
        Optional<Petsitter> petsitterOptional = petsitterRepository.findById(petsitterDto.getId());

        PetsitterDto oldPetsitterDto = findById(petsitterDto.getId());

        Petsitter petsitter = petsitterOptional.get();

        //기존의 파일들 (데이터베이스에 존재하는 파일들)
        List<String> oldFileNames = oldPetsitterDto.getUploadedFileName();
        System.out.println("기존의 파일들 - oldFileNames : "+oldFileNames);

        //새로 업로드할 파일들
        List<MultipartFile> files = petsitterDto.getFiles();
        System.out.println("새로 업로드할 파일들 - files : "+files);

        //새로 업로드 해야하는 파일들
        List<String> currentUploadFileNames = fileUtil.saveSlideFiles(files);
        System.out.println("새로 업로드 해야하는 파일들 - currentUploadFileNames : "+currentUploadFileNames);

        // 화면에서 변화 없이 유지 되어야할 파일들
        List<String> uploadedFileNames = petsitterDto.getUploadedFileName();
        System.out.println("화면에서 변화 없이 유지 되어야할 파일들 - uploadedFileNames : "+uploadedFileNames);

        //유지되는 파일들  + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이 됨
        if(currentUploadFileNames != null && currentUploadFileNames.size() > 0) {

            uploadedFileNames.addAll(currentUploadFileNames);

        }

        petsitter.updateFromDTO(petsitterDto);
        petsitter.clearList();
        List<String> uploadFileNames = petsitterDto.getUploadedFileName();

        if(uploadFileNames != null && uploadFileNames.size() > 0 ){
            uploadFileNames.stream().forEach(uploadName -> {
                petsitter.addImageString(uploadName);
            });
        }

        petsitterRepository.save(petsitter);


        if(oldFileNames != null && oldFileNames.size() > 0){
            // 지워져야하는 파일 목록 찾기
            // 예전 파일들 중에 지워져야하는 파일 이름들
            List<String> removeFiles = oldFileNames
                    .stream()
                    .filter(fileNeme -> uploadedFileNames.indexOf(fileNeme) == -1).collect(Collectors.toList());

            // 실제 파일 삭제
            fileUtil.deleteFiles(removeFiles);
        }

    }


    @Override
    public List<PetsitterDto> getList() {
        String username = memberService.getAuthName();
        Long id = memberService.findByEmail(username).getId();

        List<Petsitter> petsitters = petsitterRepository.findByMemberId(id);

        List<PetsitterDto> petsitterDtos = petsitters.stream().map(
                petsitter -> {
                    return PetsitterDto.builder()
                            .id(petsitter.getId())
                            .sitterType(petsitter.isSitterType())
                            .sitterName(petsitter.getSitterName())
                            .sitterWorkAdr(petsitter.getSitterWorkAdr())
                            .createTime(petsitter.getCreateTime())
                            .build();
                }
        ).collect(Collectors.toList());

        return petsitterDtos;
    }

    @Override
    public void delete(Long id) {
        petsitterRepository.deleteById(id);
    }

    @Override
    public MemberDto findByMember(Long id) {
        Optional<Petsitter> boardOptional = petsitterRepository.findById(id);

        // Optional.isPresent()를 사용하여 값이 있는지 확인
        if (boardOptional.isPresent()) {
            Petsitter petsitter = petsitterRepository.findById(id).get();
            return MemberDto.tomemberDto(
                    petsitter.getMember()
            );
        }else {
            return null;

        }
    }


}
