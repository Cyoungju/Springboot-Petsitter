package com.example.petsitter.wish.service;


import com.example.petsitter.wish.dto.WishDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface WishService {
    void addWish(String username, Long petsitterId);
    Page<WishDto> paging(Pageable pageable);

    boolean isWishExist(Long memberId, Long petsitterId);

    void delete(Long id);
}
