package com.back.orderplz_01.orders.service;

import org.springframework.stereotype.Service;

import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import com.back.orderplz_01.orders.dto.res.OrdersDetailRes;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.repository.OrdersRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

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

	// public ResponseEntity<ApiRes<Void>> pay(CoffeeOrderReq req) {
	// 	// 커피 재고 여부 확인
	// 	Map<Long, Coffee> coffeeMap = checkCoffee(req.coffeeOrderList());
	//
	// 	// 당일 중복 주문건 합치기
	// 	boolean isBundled = checkDuplicateOrder(req, coffeeMap);
	//
	// 	// 커피 주문
	// 	if (!isBundled) {
	// 		// 새 주문 생성
	// 		req.coffeeOrderList().forEach(orderItem -> {
	// 			Coffee coffee = coffeeMap.get(orderItem.id());
	// 			Orders newOrder = new Orders(
	// 				req.email(),
	// 				req.address(),
	// 				req.zipCode(),
	// 				null,  // orderedAt (@CreatedDate 자동)
	// 				coffee.getPrice() * orderItem.quantity(),
	// 				orderItem.quantity(),
	// 				OrderStatus.PROCESSING,
	// 				coffee
	// 			);
	// 			ordersRepository.save(newOrder);
	//
	// 			// 커피 재고 차감
	// 			coffee.decreaseQuantity(orderItem.quantity());
	// 		});
	// 	}
	//
	// 	// ApiResponse에 오후 2시 기준 메시지 담기
	// 	return ResponseEntity.ok(new ApiRes<>("주문이 완료되었습니다.", null));
	// }
	//
	// private boolean checkDuplicateOrder(CoffeeOrderReq req,  Map<Long, Coffee> coffeeMap) {
	// 	LocalDateTime now = LocalDateTime.now();
	// 	LocalDateTime todayAt2pm = now.toLocalDate().atTime(14, 0);
	//
	// 	if (now.isAfter(todayAt2pm)) {
	// 		return false; // 오후 2시 이후면 묶음 불가
	// 	}
	//
	// 	LocalDateTime start = todayAt2pm.minusDays(1);
	// 	LocalDateTime end = todayAt2pm;
	//
	// 	// 배송전 상태
	// 	// 전날 오후 2시부터 오늘 오후 2시 사이에서
	// 	// 동일한 이메일, 주소, 우편 번호의 주문이 존재하는지 확인
	//
	// 	Optional<Orders> bundleOrder = ordersRepository.findBundleTarget(
	// 		req.email(), req.address(), req.zipCode(), start, end
	// 	);
	//
	// 	bundleOrder.ifPresent(existingOrder -> {
	// 		req.coffeeOrderList().forEach(orderItem -> {
	// 			Coffee coffee = coffeeMap.get(orderItem.id());
	//
	// 			if (existingOrder.getCoffee().getId().equals(orderItem.id())) {
	// 				// 같은 커피면 수량 + 총 가격 합산
	// 				existingOrder.addQuantityAndAmount(
	// 					orderItem.quantity(),
	// 					coffee.getPrice() * orderItem.quantity()
	// 				);
	// 			}
	// 		});
	// 	});
	//
	// 	return bundleOrder.isPresent();
	// }
	//
	// private Map<Long, Coffee> checkCoffee(List<CoffeeOrderList> orders) {
	// 	// 주문한 커피
	// 	List<Long> ids = orders.stream()
	// 		.map(CoffeeOrderList::id)
	// 		.toList();
	//
	// 	// 재고에 존재하는 커피
	// 	List<Coffee> coffees = coffeeRepository.findAllById(ids);
	//
	// 	// 주문한 커피가 등록된 커피인지 확인
	// 	if (ids.size() != coffees.size()) {
	// 		List<Long> foundIds = coffees.stream()
	// 			.map(Coffee::getId)
	// 			.toList();
	//
	// 		List<Long> notFounds = ids.stream()
	// 			.filter(id -> !foundIds.contains(id))
	// 			.toList();
	//
	// 		throw new IllegalArgumentException("존재하지 않는 커피가 포함돼 있습니다. id: " + notFounds);
	// 	}
	//
	// 	// 재고 체크(주문한 커피, 재고에 존재하는 커피)
	// 	Map<Long, Coffee> coffeeMap = coffees.stream()
	// 		.collect(Collectors.toMap(Coffee::getId, coffee -> coffee));
	//
	// 	orders.forEach(orderCoffee -> {
	// 		Coffee coffee = coffeeMap.get(orderCoffee.id());
	// 		if (orderCoffee.quantity() > coffee.getQuantity()) { // 주문 수량이 재고 수량보다 많은 경우
	// 			throw new IllegalArgumentException("재고 부족. coffeeName: " + coffee.getName());
	// 		}
	// 	});
	//
	// 	return coffeeMap;
	// }
}
