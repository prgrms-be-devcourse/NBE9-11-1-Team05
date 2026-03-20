package com.back.orderplz_01.orders.service;

import java.util.List;

import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.dto.OrdersSummaryDto;
import com.back.orderplz_01.orders.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {

	private final OrdersRepository ordersRepository;

	public List<OrdersSummaryDto> findAll() {
		return ordersRepository.findAll()
			.stream()
			.map(OrdersSummaryDto::from)
			.toList();
	}

	public List<OrdersSummaryDto> findByEmail(String email) {
		return ordersRepository.findByEmail(email)
			.stream()
			.map(OrdersSummaryDto::from)
			.toList();
	}
}
