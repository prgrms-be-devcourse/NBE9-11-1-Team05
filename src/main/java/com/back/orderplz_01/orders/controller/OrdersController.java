package com.back.orderplz_01.orders.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.orderplz_01.orders.dto.OrderSearchDto;
import com.back.orderplz_01.orders.dto.res.OrdersDetailRes;
import com.back.orderplz_01.orders.service.OrderSearchService;
import com.back.orderplz_01.orders.service.OrdersService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "OrdersController", description = "주문 API")
public class OrdersController {

	private final OrdersService ordersService;
	private final OrderSearchService orderSearchService;

	// @PostMapping
	// @Operation(summary = "원두 결제하기")
	// @Transactional
	// public ResponseEntity<ApiRes<Void>> pay(@RequestBody @Valid CoffeeOrderReq req) {
	// 	return ordersService.pay(req);
	// }

	@GetMapping("/{ordersId}")
	public OrdersDetailRes ordersDetail(@PathVariable Long ordersId) {
		return ordersService.ordersDetail(ordersId);
	}

	/** CUS-09 주문 검색 API */
	@PostMapping("/search")
	public OrderSearchDto.Response search(@RequestBody OrderSearchDto.Request request) {
		return orderSearchService.search(request);
	}

	/** CUS-09 주문 검색 API 예외 처리 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<OrderSearchDto.ApiError> handleIllegalArgument(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderSearchDto.ApiError(e.getMessage()));
	}

	/** CUS-09 주문 검색 API 예외 처리 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<OrderSearchDto.ApiError> handleNotReadable(HttpMessageNotReadableException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderSearchDto.ApiError("요청 본문이 비어 있거나 JSON 형식이 올바르지 않습니다."));
	}
}
