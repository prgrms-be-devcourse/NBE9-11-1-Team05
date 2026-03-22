package com.back.orderplz_01.orders.controller;

import java.util.List;

import com.back.orderplz_01.orders.dto.OrdersDetailDto;
import com.back.orderplz_01.orders.dto.OrdersSummaryDto;
import com.back.orderplz_01.orders.service.OrdersService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "OrdersController", description = "주문 API")
@Validated
public class OrdersController {

	private final OrdersService ordersService;

	// CUS-09: 이메일 기준 주문 내역 조회
	@GetMapping("/by-email")
	public List<OrdersSummaryDto> getOrdersByEmail(
		@RequestParam @Email @NotBlank String email
	) {
		return ordersService.findByEmail(email);
	}

	// CUS-09(확장): 이메일 + 우편번호 검증 후 해당 조건의 주문 내역(헤더+라인) 한번에 조회
	@GetMapping("/by-email-and-zip")
	public List<OrdersDetailDto> getOrdersByEmailAndZip(
		@RequestParam @Email @NotBlank String email,
		@RequestParam @NotBlank @Size(max = 20) String zipCode
	) {
		return ordersService.findDetailsByEmailAndZipCode(email, zipCode);
	}

	// CUS-09: (API 네이밍) 이메일 + 우편번호 검증 후 해당 조건의 주문 내역(헤더+라인) 한번에 조회
	@GetMapping("/history")
	public List<OrdersDetailDto> getOrdersHistory(
		@RequestParam @Email @NotBlank String email,
		@RequestParam @NotBlank @Size(max = 20) String zipCode
	) {
		return ordersService.findDetailsByEmailAndZipCode(email, zipCode);
	}

	// CUS-10: 주문번호 1건 상세 조회 (헤더 + items)
	@GetMapping("/{ordersId:\\d+}")
	public OrdersDetailDto getOrdersDetail(@PathVariable Long ordersId) {
		return ordersService.findDetailByOrdersId(ordersId);
	}
}
