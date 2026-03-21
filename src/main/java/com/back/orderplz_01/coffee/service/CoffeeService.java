package com.back.orderplz_01.coffee.service;

import com.back.orderplz_01.coffee.dto.CoffeeDetailDto;
import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoffeeService {
    private final CoffeeRepository coffeeRepository;

    public CoffeeDetailDto getCoffeeDetail(Long id) {
        Coffee coffee = coffeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException( id+"번 원두는 존재하지 않습니다."));

        return new CoffeeDetailDto(
                coffee.getName(),
                coffee.getDescription(),
                coffee.getPrice(),
                coffee.getQuantity()
        );
    }
}
