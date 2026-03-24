package com.back.orderplz_01.orders.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.back.orderplz_01.orders.dto.request.OrderSearchRequestDto;
import com.back.orderplz_01.orders.dto.response.OrderSearchResponseDto;
import com.back.orderplz_01.orders.dto.res.OrderSearchLineItemRes;
import com.back.orderplz_01.orders.dto.res.OrderSearchRes;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.entity.OrdersItem;
import com.back.orderplz_01.orders.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderSearchService {

	private final OrdersRepository ordersRepository;

	public OrderSearchResponseDto search(OrderSearchRequestDto request) {
		String email = request.email().trim();
		String address = request.address().trim();
		String zipCode = request.zipCode().trim();

		List<Orders> found = ordersRepository.findOrdersForList(email, address, zipCode);
		if (found.isEmpty()) {
			return new OrderSearchResponseDto("주문한 상품이 없습니다", List.of());
		}
		List<OrderSearchRes> summaries = found.stream().map(this::toSummary).collect(Collectors.toList());
		return new OrderSearchResponseDto(null, summaries);
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
