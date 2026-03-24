package com.back.orderplz_01.orders.controller;

import com.back.orderplz_01.global.apiRes.ApiRes;
import com.back.orderplz_01.orders.dto.request.OrderStatusChangeReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.back.orderplz_01.orders.dto.res.OrdersDetailRes;
import com.back.orderplz_01.orders.service.OrdersService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "OrdersController", description = "주문 API")
public class OrdersController {

	private final OrdersService ordersService;

	@GetMapping("/{ordersId}")
	public OrdersDetailRes ordersDetail(@PathVariable Long ordersId) {
		return ordersService.ordersDetail(ordersId);
	}

	// OWN-04 : 주문 상태 변경 API
	@PatchMapping("/{id}/status")
	@Operation(
			summary = "주문 상태 변경",
			description = "PROCESSING → SHIPPED → DELIVERED 순으로 상태 변경"
	)
	public ResponseEntity<ApiRes<OrdersDetailRes>> changeStatus(
			@Parameter(description = "주문 ID")
			@PathVariable Long id,
			@RequestBody @Valid OrderStatusChangeReq request
	) {
		OrdersDetailRes updatedStatus = ordersService.changeStatus(id, request.status());
		return ResponseEntity.ok(new ApiRes<>("주문 상태 변경 성공", updatedStatus));
	}
}
