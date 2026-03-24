package com.back.orderplz_01.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.orderplz_01.global.apiRes.ApiRes;
import com.back.orderplz_01.orders.dto.request.CoffeeOrderReq;
import com.back.orderplz_01.orders.dto.request.OrderSearchRequestDto;
import com.back.orderplz_01.orders.dto.request.OrderStatusChangeReq;
import com.back.orderplz_01.orders.dto.response.OrdersDetailRes;
import com.back.orderplz_01.orders.dto.response.OrdersSearchListRes;
import com.back.orderplz_01.orders.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "OrdersController", description = "주문 API")
public class OrdersController {

	private final OrdersService ordersService;

	@PostMapping
	@Operation(summary = "원두 결제")
	public ResponseEntity<ApiRes<Void>> pay(@RequestBody @Valid CoffeeOrderReq req) {
		ordersService.pay(req);
		return ResponseEntity.ok(new ApiRes<>("원두 주문이 완료되었습니다.", null));
	}

	@GetMapping("/{ordersId}")
	@Operation(summary = "주문 상세 내역 조회")
	public ResponseEntity<ApiRes<OrdersDetailRes>> ordersDetail(@PathVariable Long ordersId) {
		return ResponseEntity.ok(new ApiRes<>("주문 상세 조회 완료", ordersService.ordersDetail(ordersId)));
	}

	/** CUS-09 주문 검색 API */
	@PostMapping("/search")
	public OrdersSearchListRes search(@Valid @RequestBody OrderSearchRequestDto request) {
		return ordersService.search(request);
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
