package com.back.orderplz_01.search.customersearch.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.search.customersearch.dto.OrdersDetailDto;
import com.back.orderplz_01.search.customersearch.repository.CustomerOrderSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerOrderSearchService {

	private final CustomerOrderSearchRepository customerOrderSearchRepository;

	@Transactional(readOnly = true)
	public List<OrdersDetailDto> findDetailsByEmailAndZipCode(String email, String zipCode) {
		return customerOrderSearchRepository
			.findByEmailIgnoreCaseAndZipCodeIgnoreCaseOrderByOrderedAtDesc(email, zipCode)
			.stream()
			.map(OrdersDetailDto::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public OrdersDetailDto findDetailByOrdersId(Integer ordersId) {
		if (ordersId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 입력 사항이 존재하지 않습니다.");
		}

		Orders orders = customerOrderSearchRepository.findByIdWithCoffee(ordersId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."));

		return OrdersDetailDto.from(orders);
	}
}
