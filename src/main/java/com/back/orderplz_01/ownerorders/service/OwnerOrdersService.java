package com.back.orderplz_01.ownerorders.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.repository.OrdersRepository;
import com.back.orderplz_01.ownerorders.dto.OwnerOrderSummaryDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OwnerOrdersService {

	private final OrdersRepository ordersRepository;

	@Transactional(readOnly = true)
	public List<OwnerOrderSummaryDto> findAllOrderLinesForOwner() {
		List<Orders> orders = ordersRepository.findAllByOrderByOrderedAtDesc();
		return orders.stream()
			.map(OwnerOrderSummaryDto::from)
			.toList();
	}
}
