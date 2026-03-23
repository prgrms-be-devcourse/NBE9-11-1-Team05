package com.back.orderplz_01.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.orderplz_01.global.apiRes.ApiRes;
import com.back.orderplz_01.orders.dto.request.CoffeeOrderReq;
import com.back.orderplz_01.orders.dto.res.OrdersDetailRes;
import com.back.orderplz_01.orders.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;
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
	@Operation(summary = "원두 결제하기")
	@Transactional
	public ResponseEntity<ApiRes<Void>> pay(@RequestBody @Valid CoffeeOrderReq req) {
		return ordersService.pay(req);
	}

	@GetMapping("/{ordersId}")
	public OrdersDetailRes ordersDetail(@PathVariable Long ordersId) {
		return ordersService.ordersDetail(ordersId);
	}
}
