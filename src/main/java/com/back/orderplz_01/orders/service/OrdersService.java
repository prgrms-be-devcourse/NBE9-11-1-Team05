package com.back.orderplz_01.orders.service;

import org.springframework.stereotype.Service;

import com.back.orderplz_01.orders.dto.res.OrdersRes;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {

	private final OrdersRepository ordersRepository;

	public OrdersRes ordersDetail(Long ordersId) {
		Orders orders = ordersRepository.findById(ordersId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

		return new OrdersRes(
			orders.getId(),
			orders.getEmail(),
			orders.getAddress(),
			orders.getZipCode(),
			orders.getOrderedAt()
		);
	}
}
