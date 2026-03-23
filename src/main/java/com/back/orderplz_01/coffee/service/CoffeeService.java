package com.back.orderplz_01.coffee.service;

import com.back.orderplz_01.coffee.dto.CoffeeDetailResponse;
import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoffeeService {
    private final CoffeeRepository coffeeRepository;

    public CoffeeDetailResponse getCoffeeDetail(Long id) {
        Coffee coffee = coffeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id+"번 원두는 존재하지 않습니다."));

        return new CoffeeDetailResponse(
                coffee.getName(),
                coffee.getDescription(),
                coffee.getPrice(),
                coffee.getQuantity()
        );
    }
}
