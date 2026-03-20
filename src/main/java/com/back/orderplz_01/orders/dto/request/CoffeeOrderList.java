package com.back.orderplz_01.orders.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CoffeeOrderList(

	@NotNull
	Long id,

	@NotNull
	@Min(1)
	Long quantity
) {
}
