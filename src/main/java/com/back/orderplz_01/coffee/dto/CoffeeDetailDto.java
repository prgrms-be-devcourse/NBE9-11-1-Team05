package com.back.orderplz_01.coffee.dto;

public record CoffeeDetailDto(
        String name,
        String description,
        Long price,
        Long quantity
){}
