package com.back.orderplz_01.search.customersearch.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.back.orderplz_01.search.customersearch.dto.OrdersDetailDto;
import com.back.orderplz_01.search.customersearch.service.CustomerOrderSearchService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "CustomerOrderSearch", description = "고객 주문 조회 — CUS-09: 이메일+우편번호 필수")
@Validated
public class CustomerOrderSearchController {

	private final CustomerOrderSearchService customerOrderSearchService;

	@GetMapping({ "/history", "/by-email-and-zip" })
	public List<OrdersDetailDto> getOrdersHistory(
		@RequestParam @Email @NotBlank String email,
		@RequestParam @NotBlank @Size(max = 20) String zipCode
	) {
		return customerOrderSearchService.findDetailsByEmailAndZipCode(email, zipCode);
	}

	@GetMapping("/{ordersId:\\d+}")
	public OrdersDetailDto getOrdersDetail(@PathVariable Integer ordersId) {
		return customerOrderSearchService.findDetailByOrdersId(ordersId);
	}
}
