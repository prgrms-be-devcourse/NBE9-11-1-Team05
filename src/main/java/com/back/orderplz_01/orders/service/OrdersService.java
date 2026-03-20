package com.back.orderplz_01.orders.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import com.back.orderplz_01.orders.dto.request.CoffeeOrderList;
import com.back.orderplz_01.orders.dto.request.CoffeeOrderReq;
import com.back.orderplz_01.orders.dto.response.CoffeeOrderRes;
import com.back.orderplz_01.orders.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {

	private final OrdersRepository ordersRepository;
	private final CoffeeRepository coffeeRepository;

	public CoffeeOrderRes pay(CoffeeOrderReq req) {
		// 커피 재고 여부 확인
		checkCoffee(req.coffeeOrderList());

		// 당일 중복 주문건 합치기

		// 커피 주문

		// ApiResponse에 오후 2시 기준 메시지 담기
	}

	private void checkCoffee(List<CoffeeOrderList> orders) {
		// 주문한 커피
		List<Long> ids = orders.stream()
			.map(CoffeeOrderList::id)
			.toList();

		// 재고에 존재하는 커피
		List<Coffee> coffees = coffeeRepository.findAllById(ids);

		// 주문한 커피가 등록된 커피인지 확인
		if(ids.size() != coffees.size()){
			List<Long> foundIds = coffees.stream()
				.map(Coffee::getId)
				.toList();

			List<Long> notFounds = ids.stream()
				.filter(id -> !foundIds.contains(id))
				.toList();

			throw new IllegalArgumentException("존재하지 않는 커피가 포함돼 있습니다. id: " + notFounds);
		}

		// 재고 체크(주문한 커피, 재고에 존재하는 커피)
		Map<Long, Coffee> coffeeMap = coffees.stream()
				.collect(Collectors.toMap(Coffee::getId, coffee -> coffee));
		orders.forEach(orderCoffee -> {
			Coffee coffee = coffeeMap.get(orderCoffee.id());
			if(orderCoffee.quantity() > coffee.getQuantity()){ // 주문 수량이 재고 수량보다 많은 경우
				throw new IllegalArgumentException("재고 부족. coffeeName: " + coffee.getName());
			}
		});
	}
}
