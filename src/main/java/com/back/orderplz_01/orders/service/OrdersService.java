package com.back.orderplz_01.orders.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import com.back.orderplz_01.global.apiRes.ApiRes;
import com.back.orderplz_01.orders.dto.request.CoffeeOrderList;
import com.back.orderplz_01.orders.dto.request.CoffeeOrderReq;
import com.back.orderplz_01.orders.dto.response.OrdersDetailRes;
import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.entity.OrdersItem;
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

	@Transactional
	public ResponseEntity<ApiRes<Void>> pay(CoffeeOrderReq req) {
		// 커피 재고 여부 확인
		Map<Long, Coffee> coffeeMap = checkCoffee(req.coffeeOrderList());

		// 당일 묶음 배송
		boolean isBundled = bundleIfDuplicateOrder(req, coffeeMap);

		// 커피 주문, 묶음 배송하지 않는 주문
		if (!isBundled) {
			createNewOrder(req, coffeeMap);
		}

		// ApiResponse에 오후 2시 기준 메시지 담기
		return ResponseEntity.ok(new ApiRes<>("원두 주문이 완료되었습니다.", null));
	}

	private void createNewOrder(CoffeeOrderReq req, Map<Long, Coffee> coffeeMap) {
		Orders newOrder = Orders.create(req.email(), req.address(), req.zipCode());

		// 주문한 커피 리스트
		req.coffeeOrderList().forEach(orderItem -> {
			Coffee coffee = coffeeMap.get(orderItem.id());
			newOrder.addOrderItem(
				orderItem.quantity(),
				coffee.getPrice(),
				coffee
			);
			coffee.decreaseQuantity(orderItem.quantity());
		});

		ordersRepository.save(newOrder);
	}

	private boolean bundleIfDuplicateOrder(CoffeeOrderReq req, Map<Long, Coffee> coffeeMap) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime todayAt2pm = now.toLocalDate().atTime(18, 0);

		if (now.isAfter(todayAt2pm)) {
			return false; // 오후 2시 이후면 묶음 불가
		}

		LocalDateTime start = todayAt2pm.minusDays(1);
		LocalDateTime end = todayAt2pm;

		// 배송전 상태
		// 전날 오후 2시부터 오늘 오후 2시 사이에서
		// 동일한 이메일, 주소, 우편 번호의 주문이 존재하는지 확인

		Optional<Orders> bundleOrder = ordersRepository.findBundleTarget(
			req.email(), req.address(), req.zipCode(), OrderStatus.PROCESSING, start, end
		);

		bundleOrder.ifPresent(existingOrder -> {
			req.coffeeOrderList().forEach(orderItem -> {
				Coffee coffee = coffeeMap.get(orderItem.id());

				Optional<OrdersItem> existingBundledCoffee = existingOrder.getOrderItems().stream()
					.filter(ordersItem -> ordersItem.getCoffee().getId().equals(coffee.getId()))
					.findFirst();

				if (existingBundledCoffee.isPresent()) { // 묶음 배송할 주문에 동일한 커피가 있을 경우
					existingBundledCoffee.get().addQuantity(orderItem.quantity());
					existingOrder.addTotalAmount(coffee.getPrice() * orderItem.quantity());
				} else { // 묶음 배송할 주문에 동일한 커피가 없을 경우
					existingOrder.addOrderItem(
						orderItem.quantity(),
						coffee.getPrice(),
						coffee
					);
				}

				coffee.decreaseQuantity(orderItem.quantity());
			});
		});

		// 묶음 배송할 주문이 없다면
		return bundleOrder.isPresent();
	}

	private Map<Long, Coffee> checkCoffee(List<CoffeeOrderList> orders) {
		// 주문한 커피
		List<Long> ids = orders.stream()
			.map(CoffeeOrderList::id)
			.toList();

		// 재고에 존재하는 커피
		List<Coffee> coffees = coffeeRepository.findAllById(ids);

		// 주문한 커피가 등록된 커피인지 확인
		if (ids.size() != coffees.size()) {
			List<Long> foundIds = coffees.stream()
				.map(Coffee::getId)
				.toList();

			List<Long> notFounds = ids.stream()
				.filter(id -> !foundIds.contains(id))
				.toList();

			throw new EntityNotFoundException("존재하지 않는 커피가 포함돼 있습니다. id: " + notFounds);
		}

		// 재고 체크(주문한 커피, 재고에 존재하는 커피)
		Map<Long, Coffee> coffeeMap = coffees.stream()
			.collect(Collectors.toMap(Coffee::getId, coffee -> coffee));

		orders.forEach(orderCoffee -> {
			Coffee coffee = coffeeMap.get(orderCoffee.id());
			if (orderCoffee.quantity() > coffee.getQuantity()) { // 주문 수량이 재고 수량보다 많은 경우
				throw new IllegalArgumentException("재고 부족. coffeeName: " + coffee.getName());
			}
		});

		return coffeeMap;
	}
	// 주문이 존재해야 배송 상태 변경 가능하므로 조회 후 처리
	@Transactional
	public void changeStatus(Long orderId, OrderStatus newStatus) {

		Orders order = ordersRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("주문 없음"));

		order.changeStatus(newStatus);
	}

}
