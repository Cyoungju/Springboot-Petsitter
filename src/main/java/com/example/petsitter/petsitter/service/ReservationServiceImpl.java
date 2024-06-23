package com.example.petsitter.petsitter.service;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.service.MemberService;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.domain.Reservation;
import com.example.petsitter.petsitter.dto.PetsitterDto;
import com.example.petsitter.petsitter.dto.ReservationDto;
import com.example.petsitter.petsitter.repository.PetsitterRepository;
import com.example.petsitter.petsitter.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private  final PetsitterRepository petsitterRepository;

    @Override
    public List<LocalTime> getTimesByDateAndPetsitter(LocalDate date, Long petsitterId) {
        return reservationRepository.findTimesByDateAndPetsitter(date, petsitterId);
    }
       /*
    @Override
    public void save(ReservationDto reservationDto) {

        //로그인한 회원정보 가져오기
        String username = memberService.getAuthName();
        Member member = memberService.findByEmail(username);

        // petsitter상품 가져오기
        Long petsitterId = reservationDto.getPetsitterId();
        Petsitter petsitter = petsitterRepository.findById(petsitterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 해당하는 펫시터가 존재하지 않습니다: " + petsitterId));

        // 저장된 시간 가져오기
        LocalDate date = reservationDto.getDate();
        List<LocalTime> timesToAdd = reservationDto.getTimes();

        Optional<Reservation> optionalReservation = reservationRepository.findByDateAndPetsitters(date, petsitter);
        if (optionalReservation.isPresent()) {
            Reservation existingReservation = optionalReservation.get();
            //만약 member가 로그인한 회원정보와 동일하다면
            if (existingReservation.getMembers().stream().anyMatch(m -> m.getId().equals(member.getId()))) {
                //DTO 생성 - 변환
                ReservationDto existingReservationDto = ReservationDto.fromEntity(existingReservation);
                List<LocalTime> existingTimes = existingReservationDto.getTimes();
                existingTimes.addAll(timesToAdd);
                // 중복 제거된 시간 리스트
                existingTimes = existingTimes.stream().distinct().collect(Collectors.toList());
                // 시간 업데이트
                existingReservationDto.setTimes(existingTimes);

                reservationRepository.save(existingReservationDto.toEntity(member, petsitter));
            } else {
                // 다른 회원인 경우 새로운 예약 생성
                reservationRepository.save(reservationDto.toEntity(member, petsitter));
            }
        } else {
            // 기존 예약이 없는 경우 새로운 예약 생성
            reservationRepository.save(reservationDto.toEntity(member, petsitter));
        }

    }


    @Override
    public List<LocalTime> getTimesByDateAndPetsitter(LocalDate date, Long petsitterId) {

        List<LocalTime> findByDateAndPetsitter = reservationRepository.findTimesByDateAndPetsitters(date, petsitterId);
        return findByDateAndPetsitter;
    }

    @Override
    public Long calculateTotalPriceByTime(Long petsitterId, List<LocalTime> time) {
        Petsitter petsitter = petsitterRepository.findById(petsitterId).get();

        Long totalPrice = petsitter.getSitterPrice() * time.size();


        return totalPrice;
    }
    */
}
