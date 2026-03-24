package com.back.orderplz_01.orders.service;

import com.back.orderplz_01.orders.entity.OrderStatus;
import org.springframework.stereotype.Service;

import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import com.back.orderplz_01.orders.dto.res.OrdersDetailRes;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.repository.OrdersRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrdersService {

	private final OrdersRepository ordersRepository;
	private final CoffeeRepository coffeeRepository;

	public OrdersDetailRes ordersDetail(Long ordersId) {
		Orders orders = ordersRepository.findById(ordersId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다."));

		return OrdersDetailRes.from(orders);
	}

	// OWN-04 : 주문 상태 변경
	@Transactional
	public OrdersDetailRes changeStatus(Long orderId, OrderStatus newStatus) {

		Orders order = ordersRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("주문 없음"));

		order.changeStatus(newStatus);

		return OrdersDetailRes.from(order);
	}

}
