package com.back.orderplz_01.orders.dto.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CoffeeOrderReq(

	@Email
	@NotBlank(message = "이메일은 필수 값입니다.")
	String email,

	@NotBlank(message = "주소는 필수 값입니다.")
	String address,

	@NotBlank(message = "우편번호는 필수 값입니다.")
	String zipCode,

	@NotEmpty(message = "원두를 최소 한개 이상 주문해야합니다.")
	List<CoffeeOrderList> coffeeOrderList
) {
}
