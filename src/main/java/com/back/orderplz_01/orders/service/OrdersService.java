package com.back.orderplz_01.orders.service;

import org.springframework.stereotype.Service;

import com.back.orderplz_01.orders.dto.res.OrdersDetailRes;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.repository.OrdersRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {

	private final OrdersRepository ordersRepository;

	public OrdersDetailRes ordersDetail(Long ordersId) {
		Orders orders = ordersRepository.findById(ordersId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다."));

		return OrdersDetailRes.from(orders);
	}
}
