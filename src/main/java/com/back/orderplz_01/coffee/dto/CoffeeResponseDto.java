package com.back.orderplz_01.coffee.dto;

import com.back.orderplz_01.coffee.entity.Coffee;

public record CoffeeResponseDto(
        Long id,
        String name,
        String description,
        Long price,
        Long quantity

) {
    public static CoffeeResponseDto from(Coffee coffee) {
        return new CoffeeResponseDto(
                coffee.getId(),
                coffee.getName(),
                coffee.getDescription(),
                coffee.getPrice(),
                coffee.getQuantity()

        );
    }
}