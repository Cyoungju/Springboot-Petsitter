package com.example.petsitter.reservation.service;


import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.repository.MemberRepository;
import com.example.petsitter.petsitter.domain.Petsitter;
import com.example.petsitter.petsitter.domain.PetsitterReservation;
import com.example.petsitter.reservation.domain.Reservation;
import com.example.petsitter.reservation.domain.ReservationStatus;
import com.example.petsitter.reservation.domain.ReservationTime;
import com.example.petsitter.reservation.dto.ReservationDto;
import com.example.petsitter.petsitter.repository.PetsitterRepository;
import com.example.petsitter.petsitter.repository.PetsitterReservationRepository;
import com.example.petsitter.reservation.item.Item;
import com.example.petsitter.reservation.item.ItemRepository;
import com.example.petsitter.reservation.repository.ReservationRepository;
import com.example.petsitter.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final PetsitterRepository petsitterRepository;
    private final PetsitterReservationRepository petsitterReservationRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<LocalTime> getTimesByDateAndPetsitter(LocalDate date, Long petsitterId) {
        return reservationRepository.findTimesByDateAndNotConfirmed(date, petsitterId);
    }

    @Override
    public void addReservation(String username, ReservationDto reservationDto) {
        // 로그인한 회원 정보 가져오기
        Member member = memberRepository.findByEmail(username);
        if (member == null) {
            throw new IllegalArgumentException("해당 이메일에 해당하는 회원이 존재하지 않습니다: " + username);
        }

        // 펫시터 상품 가져오기
        Long petsitterId = reservationDto.getPetsitterId();
        Petsitter petsitter = petsitterRepository.findById(petsitterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 해당하는 펫시터가 존재하지 않습니다: " + petsitterId));

        // 저장된 시간 가져오기
        LocalDate date = reservationDto.getDate();
        List<LocalTime> timesToAdd = reservationDto.getTimes();


        Reservation reservation = Reservation.builder()
                .date(date)
                .times(timesToAdd)
                .totalPrice(petsitter.getSitterPrice() * timesToAdd.size())
                .status(ReservationStatus.대기)
                .member(member)
                .build();

        reservationRepository.save(reservation);
        // 중간 테이블에 저장
        PetsitterReservation petsitterReservation = new PetsitterReservation(petsitter, reservation);
        petsitterReservationRepository.save(petsitterReservation);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        List<String> times = reservation.getTimes().stream().map(
                time -> time.format(formatter)
        ).collect(Collectors.toList());


        //아이템 저장
        Item item = Item.builder()
                .reservation(reservation)
                .times(times)
                .petsitter(petsitterReservation)
                .timeCount(reservation.getTimes().stream().count())
                .price(reservation.getTotalPrice() / reservation.getTimes().stream().count())
                .build();

        itemRepository.save(item);
    }

    @Override
    public Long calculateTotalPriceByTime(Long petsitterId, List<LocalTime> time) {
        Petsitter petsitter = petsitterRepository.findById(petsitterId).get();

        Long totalPrice = petsitter.getSitterPrice() * time.size();

        return totalPrice;
    }


    //회원에 맞는 예약건 가져오기
    @Override
    public List<Item> findAllItemsByMember(Member member) {
        return itemRepository.findAllByReservationMember(member);
    }

    @Override
    public List<Item> findPetsitterReservation(Member member) {

        return itemRepository.findAllByReservation_Petsitters_Petsitter_Member(member);
    }

    @Override
    public void updateStatus(String status, Long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            reservation.updateStatusDTO(status);

            System.out.println(status);

            reservationRepository.save(reservation);
        }
    }

}
