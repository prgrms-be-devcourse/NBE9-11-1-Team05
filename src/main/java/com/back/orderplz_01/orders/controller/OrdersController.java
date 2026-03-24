package com.back.orderplz_01.orders.controller;

import com.back.orderplz_01.orders.dto.request.OrderSearchRequestDto;
import com.back.orderplz_01.orders.dto.request.OrderStatusChangeReq;
import com.back.orderplz_01.orders.dto.res.OrdersDetailRes;
import com.back.orderplz_01.orders.dto.response.OrderSearchResponseDto;
import com.back.orderplz_01.orders.service.OrderSearchService;
import com.back.orderplz_01.orders.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "OrdersController", description = "주문 API")
public class OrdersController {

	private final OrdersService ordersService;
	private final OrderSearchService orderSearchService;

	@GetMapping("/{ordersId}")
	public OrdersDetailRes ordersDetail(@PathVariable Long ordersId) {
		return ordersService.ordersDetail(ordersId);
	}

	/** CUS-09 주문 검색 API */
	@PostMapping("/search")
	public OrderSearchResponseDto search(@Valid @RequestBody OrderSearchRequestDto request) {
		return orderSearchService.search(request);
	}

	@PatchMapping("/{id}/status")
	@Operation(
			summary = "주문 상태 변경",
			description = "PROCESSING → SHIPPED → DELIVERED 순으로 상태 변경"
	)
	public ResponseEntity<Void> changeStatus(
			@Parameter(description = "주문 ID")
			@PathVariable Long id,
			@RequestBody @Valid OrderStatusChangeReq request
	) {
		ordersService.changeStatus(id, request.status());
		return ResponseEntity.ok().build();
	}
}
