package com.example.petsitter.petsitter.service;

import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.petsitter.dto.PetsitterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PetsitterService {
    void save(PetsitterDto petsitterDto);

    Page<PetsitterDto> paging (Pageable pageable);

    Page<PetsitterDto> pagingSearchList(String searchKeyword, Pageable pageable);

    PetsitterDto findById(Long id);

    void update(PetsitterDto petsitterDto);

    List<PetsitterDto> getList();

    void delete(Long id);

    MemberDto findByMember(Long id);
}
