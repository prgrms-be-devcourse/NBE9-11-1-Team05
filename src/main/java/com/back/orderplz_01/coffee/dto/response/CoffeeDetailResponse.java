package com.back.orderplz_01.coffee.dto.response;

import com.back.orderplz_01.coffee.entity.Coffee;

public record CoffeeDetailResponse(
        String name,
        String description,
        Long price,
        Long quantity
){
    public static CoffeeDetailResponse from(Coffee coffee) {
        return new CoffeeDetailResponse(
                coffee.getName(),
                coffee.getDescription(),
                coffee.getPrice(),
                coffee.getQuantity()
        );
    }
}
