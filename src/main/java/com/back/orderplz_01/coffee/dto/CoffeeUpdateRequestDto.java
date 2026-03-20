package com.back.orderplz_01.coffee.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CoffeeUpdateRequestDto(
        @NotBlank(message = "원두 이름은 필수입니다.")
        String name,

        @NotBlank(message = "원두 설명은 필수입니다.")
        String description,

        @NotNull(message = "원두 가격은 필수입니다.")
        @Min(value = 0, message = "원두 가격은 0 이상이어야 합니다.")
        Long price,

        @NotNull(message = "원두 수량은 필수입니다.")
        @Min(value = 0, message = "원두 수량은 0 이상이어야 합니다.")
        Long quantity,

        @NotBlank(message = "이미지 URL은 필수입니다.")
        String imageUrl
) {
}