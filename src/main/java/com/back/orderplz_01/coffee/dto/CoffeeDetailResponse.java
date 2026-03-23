package com.back.orderplz_01.coffee.dto;

public record CoffeeDetailResponse(
        String name,
        String description,
        Long price,
        Long quantity
){}
