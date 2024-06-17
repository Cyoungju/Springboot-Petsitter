package com.example.petsitter.pet.service;


import com.example.petsitter.pet.dto.PetDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PetService {

    void save(PetDto petDto);

    List<PetDto> getList();
}
