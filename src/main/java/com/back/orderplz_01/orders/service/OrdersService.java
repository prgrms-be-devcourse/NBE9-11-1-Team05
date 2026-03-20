package com.back.orderplz_01.orders.service;

import java.util.List;

import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.dto.OrdersSummaryDto;
import com.back.orderplz_01.orders.dto.OrdersDetailDto;
import com.back.orderplz_01.orders.repository.OrdersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {

	private final OrdersRepository ordersRepository;

	public List<OrdersSummaryDto> findAll() {
		return toSummaryDtos(ordersRepository.findAllByOrderByOrderedAtDesc());
	}

	public List<OrdersSummaryDto> findByEmail(String email) {
		return toSummaryDtos(ordersRepository.findByEmailIgnoreCaseOrderByOrderedAtDesc(email));
	}

	public List<OrdersDetailDto> findDetailsByEmailAndZipCode(String email, String zipCode) {
		List<Orders> orders = ordersRepository.findByEmailIgnoreCaseAndZipCodeIgnoreCaseOrderByOrderedAtDesc(
			email,
			zipCode
		);
		
		return orders.stream()
			.map(OrdersDetailDto::from)
			.toList();
	}

	private List<OrdersSummaryDto> toSummaryDtos(List<Orders> orders) {
		return orders.stream()
			.map(OrdersSummaryDto::from)
			.toList();
	}

	public OrdersDetailDto findDetailByOrdersId(Long ordersId) {
		if (ordersId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 입력 사항이 존재하지 않습니다.");
		}
		if (ordersId > Integer.MAX_VALUE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "벗어난 범위입니다.");
		}

		Orders orders = ordersRepository.findById(ordersId.intValue())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."));

		return OrdersDetailDto.from(orders);
	}
}
