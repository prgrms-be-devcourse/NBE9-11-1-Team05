package com.back.orderplz_01.orders.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import com.back.orderplz_01.orders.dto.request.OrderSearchRequestDto;
import com.back.orderplz_01.orders.dto.response.OrderSearchLineItemRes;
import com.back.orderplz_01.orders.dto.response.OrderSearchResponseDto;
import com.back.orderplz_01.orders.dto.response.OrderSearchRes;
import com.back.orderplz_01.orders.dto.res.OrdersDetailRes;
import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.entity.OrdersItem;
import com.back.orderplz_01.orders.repository.OrdersRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class OrdersService {

	private final OrdersRepository ordersRepository;

	public OrdersDetailRes ordersDetail(Long ordersId) {
		Orders orders = ordersRepository.findById(ordersId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다."));

		return OrdersDetailRes.from(orders);
	}

	// 주문이 존재해야 배송 상태 변경 가능하므로 조회 후 처리
	@Transactional
	public void changeStatus(Long orderId, OrderStatus newStatus) {

		Orders order = ordersRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("주문 없음"));

		order.changeStatus(newStatus);
	}

	// ---------------------------------------------------------------------------
	// CUS-09 내 주문 목록 검색 (이메일 배송지 주소 우편번호)
	// - 주문 요약 + 라인 아이템(원두명, 수량, 가격) 목록 반환 (없으면 빈 목록)

	@Transactional(readOnly = true)
	public OrderSearchResponseDto search(OrderSearchRequestDto request) {
		String email = request.email().trim();
		String address = request.address().trim();
		String zipCode = request.zipCode().trim();

		List<Orders> found = ordersRepository.findOrdersForList(email, address, zipCode);
		List<OrderSearchRes> summaries = 
				found.stream()
				.map(this::toSummary)
				.collect(Collectors.toList());

		return new OrderSearchResponseDto(summaries);
	}

	private OrderSearchRes toSummary(Orders order) {
		List<OrderSearchLineItemRes> lines = order.getOrderItems() == null ? List.of()
				: order.getOrderItems().stream().filter(Objects::nonNull).map(this::toLine).collect(Collectors.toList());
		return new OrderSearchRes(order.getId(), order.getCreateDate(), order.getOrderStatus(), lines,
				order.getAddress(), order.getZipCode(), order.getTotalAmount());
	}

	private OrderSearchLineItemRes toLine(OrdersItem item) {
		String coffeeName = item.getCoffee() != null ? item.getCoffee().getName() : null;
		return new OrderSearchLineItemRes(coffeeName, item.getQuantity(), item.getPrice());
	}

}
