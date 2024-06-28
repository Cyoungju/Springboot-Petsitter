package com.example.petsitter.wish;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.repository.MemberRepository;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.pet.domain.Pet;
import com.example.petsitter.pet.dto.PetDto;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.repository.PetsitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


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
        if (member == null) {
            throw new IllegalArgumentException("해당 이메일에 해당하는 회원이 존재하지 않습니다: " + username);
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
