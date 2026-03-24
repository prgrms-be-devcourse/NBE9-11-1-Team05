package com.back.orderplz_01.coffee.service;


import com.back.orderplz_01.coffee.dto.response.CoffeeDetailResponse;
import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import jakarta.persistence.EntityNotFoundException;
import com.back.orderplz_01.coffee.dto.response.CoffeeResponseDto;
import com.back.orderplz_01.coffee.dto.request.CoffeeUpdateRequestDto;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeService {
    private final CoffeeRepository coffeeRepository;

    @Transactional(readOnly = true)
    public List<CoffeeResponseDto> findAll() {
        return coffeeRepository.findAll()
                .stream()
                .map(CoffeeResponseDto::from)
                .toList();
    }




    //own-02에서 사용
    @Transactional(readOnly = true)
    public CoffeeResponseDto findById(Long id) {
        Coffee coffee = coffeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 없습니다. id=" + id));
        return CoffeeResponseDto.from(coffee);
    }

    //cus-02에서 사용
    @Transactional(readOnly = true)
    public CoffeeDetailResponse getCoffeeDetail(Long id) {
        Coffee coffee = coffeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id+"번 원두는 존재하지 않습니다."));

        return CoffeeDetailResponse.from(coffee);
    }


    //own-02에서 사용
    @Transactional
    public CoffeeResponseDto update(Long id, CoffeeUpdateRequestDto requestDto) {
        Coffee coffee = coffeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 없습니다. id=" + id));

        coffee.update(
                requestDto.name(),
                requestDto.description(),
                requestDto.price(),
                requestDto.quantity()

        );

        return CoffeeResponseDto.from(coffee);
    }

}