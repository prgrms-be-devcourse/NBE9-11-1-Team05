package com.back.orderplz_01.orders.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import com.back.orderplz_01.global.apiRes.ApiRes;
import com.back.orderplz_01.orders.dto.request.CoffeeOrderList;
import com.back.orderplz_01.orders.dto.request.CoffeeOrderReq;
import com.back.orderplz_01.orders.dto.request.OrderSearchRequestDto;
import com.back.orderplz_01.orders.dto.response.OrdersOwnerSearchItem;
import com.back.orderplz_01.orders.dto.response.OrdersOwnerSearchRes;
import com.back.orderplz_01.orders.dto.response.OrdersDetailRes;
import com.back.orderplz_01.orders.dto.response.OrdersSearchItemRes;
import com.back.orderplz_01.orders.dto.response.OrdersSearchLineItemRes;
import com.back.orderplz_01.orders.dto.response.OrdersSearchListRes;
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
	// OWN-04 : 주문 상태 변경
	@Transactional
	public OrdersDetailRes changeStatus(Long orderId, OrderStatus newStatus) {

		Orders order = ordersRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("주문 없음"));

		order.changeStatus(newStatus);

		return OrdersDetailRes.from(order);
	}

	// ----------------------------------CUS-09 고객 주문내역 조회-----------------------------------------
	// CUS-09 내 주문정보 조회 (이메일 주소 우편번호)

	@Transactional(readOnly = true)
	public OrdersSearchListRes search(OrderSearchRequestDto request) {
		String email = request.email();
		String address = request.address();
		String zipCode = request.zipCode();

		List<Orders> orders = ordersRepository.findOrdersForList(email, address, zipCode);

		List<OrdersSearchItemRes> orderList = new ArrayList<>(orders.size());
		for (Orders order : orders) {
			orderList.add(toOrdersSearchItemRes(order));
		}

		return new OrdersSearchListRes(orderList);
	}

	/* CUS-09 주문 정보 (주문번호,일자,주문상태,주문라인아이템,주소,우편번호,총금액) */
	private OrdersSearchItemRes toOrdersSearchItemRes(Orders order) {
		List<OrdersSearchLineItemRes> orderLines = new ArrayList<>();
		for (OrdersItem item : order.getOrderItems()) {
			orderLines.add(toOrdersSearchLineItemRes(item));
		}

		return new OrdersSearchItemRes(
				order.getId(),
				order.getOrderedAt(),
				order.getOrderStatus(),
				orderLines,
				order.getAddress(),
				order.getZipCode(),
				order.getTotalAmount());
	}

	/* CUS-09 주문한 아이템의 정보를 라인별로 생성 후 반환 (원두명, 수량, 가격) */
	/* ex: 에티오피아 2봉, 케냐 1봉을 같이 구매했다면? 주문 라인은 2줄 */
	private OrdersSearchLineItemRes toOrdersSearchLineItemRes(OrdersItem item) {
		return new OrdersSearchLineItemRes(
				item.getCoffee().getName(),
				item.getQuantity(),
				item.getPrice());
	}

	// ---------------------------------------------------------------------------
	// OWN-09 업주 주문관리 - 전체 주문 목록 조회
	// 결제 완료된 모든 주문 건을 보여줘야 하므로 findall 처리 Repository 사용 X
	// 서비스에서 ordersRepository.findAll로 orderedAt 기준 정렬 조회하여 
	// 해당 API에서 전체 주문 목록을 제공
	// 반환 항목: 주문번호, 주문자 이메일, 주문일시, 총 금액

	@Transactional(readOnly = true)
	public OrdersOwnerSearchRes getAllProcessingOrders() {
		List<Orders> found = ordersRepository.findAll(
			Sort.by(Sort.Direction.DESC, "orderedAt")
		);

		List<OrdersOwnerSearchItem> orderList = new ArrayList<>(found.size());
		for (Orders order : found) {
			orderList.add(toOrdersOwnerSearchItem(order));
		}

		return new OrdersOwnerSearchRes(orderList);
	}

	/* OWN-09 주문 한 건 (주문번호, 이메일, 주문일시, 총금액) */
	private OrdersOwnerSearchItem toOrdersOwnerSearchItem(Orders order) {
		return new OrdersOwnerSearchItem(
				order.getId(),
				order.getEmail(),
				order.getOrderedAt(),
				order.getTotalAmount());
	}
}