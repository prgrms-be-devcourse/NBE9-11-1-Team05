package com.back.orderplz_01.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.back.orderplz_01.global.apiRes.ApiRes;
import com.back.orderplz_01.orders.dto.request.CoffeeOrderReq;
import com.back.orderplz_01.orders.dto.request.OrderStatusChangeReq;
import com.back.orderplz_01.orders.dto.response.OrdersDetailRes;
import com.back.orderplz_01.orders.dto.response.OrdersOwnerSearchRes;
import com.back.orderplz_01.orders.dto.response.OrdersSearchListRes;
import com.back.orderplz_01.orders.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;


@Validated
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
		return ResponseEntity.ok(new ApiRes<>("원두 주문 완료", null));
	}

	/** OWN-09 업주 전체 주문 목록 */
	@GetMapping("/owner/orderlist")
	@Operation(summary = "OWN-09 업주 전체 주문 목록 조회 API")
	public ResponseEntity<ApiRes<OrdersOwnerSearchRes>> ownerOrdersList() {
		OrdersOwnerSearchRes result = ordersService.getAllProcessingOrders();
		return ResponseEntity.ok(new ApiRes<>("업주 전체 주문 목록 조회 완료", result));
	}

	/** CUS-09 주문 검색 API */
	@GetMapping("/search")
	@Operation(summary = "주문 검색 (이메일 주소 우편번호)")
	public ResponseEntity<ApiRes<OrdersSearchListRes>> search(

			@Parameter(description = "이메일")
			@RequestParam
			@NotBlank(message = "이메일을 입력해주세요.")
			@Email(message = "이메일 형식이 올바르지 않습니다.")
			String email,

			@Parameter(description = "배송지(주소)")
			@RequestParam
			@NotBlank(message = "주소를 입력해주세요.")
			String address,

			@Parameter(description = "우편번호")
			@RequestParam
			@NotBlank(message = "우편번호를 입력해주세요.")
			String zipCode
	) {
		return ResponseEntity.ok(new ApiRes<>("주문 검색 완료", ordersService.search(email, address, zipCode)));
	}

	@GetMapping("/{ordersId}")
	@Operation(summary = "주문 상세 내역 조회")
	public ResponseEntity<ApiRes<OrdersDetailRes>> ordersDetail(@PathVariable Long ordersId) {
		return ResponseEntity.ok(new ApiRes<>("주문 상세 조회 완료", ordersService.ordersDetail(ordersId)));
	}

	@DeleteMapping("/{ordersId}")
	@Operation(
		summary = "주문 삭제",
		description = "묶음 배송의 경우 전체 삭제"
	)
	public ResponseEntity<ApiRes<Void>> deleteOrders(@PathVariable Long ordersId) {
		ordersService.deleteOrders(ordersId);
		return ResponseEntity.ok(new ApiRes<>("주문 취소 완료", null));
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
