package com.back.orderplz_01.coffee.service;

import com.back.orderplz_01.coffee.dto.CoffeeResponseDto;
import com.back.orderplz_01.coffee.dto.CoffeeUpdateRequestDto;
import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Transactional(readOnly = true)
    public CoffeeResponseDto findById(Long id) {
        Coffee coffee = coffeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 없습니다. id=" + id));
        return CoffeeResponseDto.from(coffee);
    }

    @Transactional
    public CoffeeResponseDto update(Long id, CoffeeUpdateRequestDto requestDto) {
        Coffee coffee = coffeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 없습니다. id=" + id));

        coffee.update(
                requestDto.name(),
                requestDto.description(),
                requestDto.price(),
                requestDto.quantity(),
                requestDto.imageUrl()
        );

        return CoffeeResponseDto.from(coffee);
    }
}