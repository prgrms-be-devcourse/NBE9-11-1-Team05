package com.back.orderplz_01.orders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.back.orderplz_01.orders.dto.request.OrderSearchRequestDto;
import com.back.orderplz_01.orders.dto.response.OrdersSearchItemRes;
import com.back.orderplz_01.orders.dto.response.OrdersSearchLineItemRes;
import com.back.orderplz_01.orders.dto.response.OrdersSearchListRes;
import com.back.orderplz_01.orders.dto.res.OrdersDetailRes;
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

	// ---------------------------------------------------------------------------
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
}