package com.back.orderplz_01.orders.dto.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CoffeeOrderReq(

	@Email
	String email,

	@NotBlank(message = "주소는 필수 값입니다.")
	String address,

	@NotBlank(message = "우편번호는 필수 값입니다.")
	String zipCode,

	@NotBlank(message = "장바구니는 필수 값입니다.")
	@Min(1)
	List<CoffeeOrderList> coffeeOrderList
) {
}
