package com.example.petsitter.wish.service;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.repository.MemberRepository;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.repository.PetsitterRepository;
import com.example.petsitter.wish.domain.Wish;
import com.example.petsitter.wish.dto.WishDto;
import com.example.petsitter.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class WishServiceImpl implements WishService{

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final PetsitterRepository petsitterRepository;
    private final MemberService memberService;

    @Override
    public void addWish(String username, Long petsitterId){
        // 로그인한 회원 정보 가져오기
        Member member = memberRepository.findByEmail(username);
        Long memberId = member.getId();
        if (member == null) {
            throw new IllegalArgumentException("해당 이메일에 해당하는 회원이 존재하지 않습니다: " + username);
        }

        // 이미 해당 펫시터 상품에 대해 해당 회원이 Wish를 했는지 확인
        Optional<Wish> existingWish = wishRepository.findByPetsitterIdAndMemberId(petsitterId, memberId);

        if (existingWish.isPresent()) {
            // 이미 Wish가 존재할 경우 예외 처리 또는 메시지 처리
            throw new IllegalStateException("이미 해당 펫시터 상품을 찜했습니다.");
        }


        // 펫시터 상품 가져오기
        Petsitter petsitter = petsitterRepository.findById(petsitterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 해당하는 펫시터가 존재하지 않습니다: " + petsitterId));

        WishDto wishDto = new WishDto();
        wishRepository.save(wishDto.toEntity(member,petsitter));
    }


    @Override
    public Page<WishDto> paging(Pageable pageable) {
        String username = memberService.getAuthName();
        Member member = memberRepository.findByEmail(username);
        Long memberId = member.getId();

        int page = pageable.getPageNumber() - 1;
        int size = 5;

        Page<Wish> wishPage = wishRepository.findByMemberId(
                memberId, PageRequest.of(page, size, Sort.by("id").descending())
        );

        return wishPage.map(wish -> WishDto.builder()
                .id(wish.getId())
                .petsitter(wish.getPetsitter())
                .member(wish.getMember())
                .build());
    }

    @Override
    public boolean isWishExist(Long memberId, Long petsitterId) {
        return wishRepository.existsByMemberIdAndPetsitterId(memberId, petsitterId);
    }

    @Override
    public void delete(Long id) {
        wishRepository.deleteById(id);
    }


}
