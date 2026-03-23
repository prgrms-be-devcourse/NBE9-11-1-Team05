package com.back.orderplz_01.search.ownersearch.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.orderplz_01.search.ownersearch.dto.OwnerOrderSummaryDto;
import com.back.orderplz_01.search.ownersearch.service.OwnerOrderSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/orders")
@Tag(name = "OwnerOrderSearch", description = "업주 주문 조회 (OWN-09)")
public class OwnerOrderSearchController {

	private final OwnerOrderSearchService ownerOrderSearchService;

	@GetMapping
	@Operation(
		summary = "OWN-09 주문 목록 (페이지네이션)",
		description = "주문 시각 최신순. page(0~), size(기본 20), sort(선택)"
	)
	public Page<OwnerOrderSummaryDto> listAll(
		@ParameterObject @PageableDefault(size = 20, sort = "orderedAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ownerOrderSearchService.findAllOrderLinesForOwner(pageable);
	}
}
