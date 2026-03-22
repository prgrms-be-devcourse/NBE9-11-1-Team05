package com.back.orderplz_01.ownerorders.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.orderplz_01.ownerorders.dto.OwnerOrderSummaryDto;
import com.back.orderplz_01.ownerorders.service.OwnerOrdersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/orders")
@Tag(name = "OwnerOrdersController", description = "업주용 주문 API (Orders 도메인 조회)")
public class OwnerOrdersController {

	private final OwnerOrdersService ownerOrdersService;

	@GetMapping
	@Operation(
		summary = "OWN-09 전체 주문 목록",
		description = "들어온 모든 주문 라인을 최신순으로 조회합니다. 이메일, 주문시각, 총액, 배송지, 상품명, 수량, 상태 등이 포함됩니다."
	)
	public List<OwnerOrderSummaryDto> listAll() {
		return ownerOrdersService.findAllOrderLinesForOwner();
	}
}
