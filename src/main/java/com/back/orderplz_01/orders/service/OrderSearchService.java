package com.back.orderplz_01.orders.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.back.orderplz_01.orders.dto.OrderSearchDto;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.entity.OrdersItem;
import com.back.orderplz_01.orders.repository.OrdersSearchRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderSearchService {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	private static final String MSG_NO_ORDERS = "주문한 상품이 없습니다";

	private final OrdersSearchRepository ordersSearchRepository;

	public OrderSearchDto.Response search(OrderSearchDto.Request request) {
		validate(request);

		String email = request.getEmail().trim();
		String address = request.getAddress().trim();
		String zipCode = request.getZipCode().trim();

		List<Orders> found = ordersSearchRepository.findOrdersForList(email, address, zipCode);

		if (found.isEmpty()) {
			return new OrderSearchDto.Response(MSG_NO_ORDERS, List.of());
		}

		List<OrderSearchDto.OrderSummary> summaries = found.stream().map(this::toSummary).collect(Collectors.toList());
		return new OrderSearchDto.Response(null, summaries);
	}

	private void validate(OrderSearchDto.Request request) {
		List<String> problems = new ArrayList<>();
		if (request == null) {
			throw new IllegalArgumentException("요청 본문이 비어 있습니다.");
		}

		String email = request.getEmail() == null ? "" : request.getEmail().trim();
		String address = request.getAddress() == null ? "" : request.getAddress().trim();
		String zip = request.getZipCode() == null ? "" : request.getZipCode().trim();

		if (email.isEmpty()) {
			problems.add("이메일을 입력해주세요.");
		} else if (!EMAIL_PATTERN.matcher(email).matches()) {
			problems.add("이메일 형식이 올바르지 않습니다.");
		}

		if (address.isEmpty()) {
			problems.add("주소를 입력해주세요.");
		}

		if (zip.isEmpty()) {
			problems.add("우편번호를 입력해주세요.");
		}

		if (!problems.isEmpty()) {
			throw new IllegalArgumentException(String.join(" ", problems));
		}
	}

	private OrderSearchDto.OrderSummary toSummary(Orders order) {
		List<OrderSearchDto.LineItem> lines = order.getOrderItems() == null ? List.of()
				: order.getOrderItems().stream().filter(Objects::nonNull).map(this::toLine).collect(Collectors.toList());

		return new OrderSearchDto.OrderSummary(order.getId(), order.getCreateDate(), order.getOrderStatus(), lines,
				order.getAddress(), order.getZipCode(), order.getTotalAmount());
	}

	private OrderSearchDto.LineItem toLine(OrdersItem item) {
		String coffeeName = item.getCoffee() != null ? item.getCoffee().getName() : null;
		return new OrderSearchDto.LineItem(coffeeName, item.getQuantity(), item.getPrice());
	}
}

