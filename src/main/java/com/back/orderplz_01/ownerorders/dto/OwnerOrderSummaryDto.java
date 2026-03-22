package com.back.orderplz_01.ownerorders.dto;

import java.time.LocalDateTime;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.entity.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 업주(Owner) 화면용 주문 목록 항목. 영속 모델 {@link Orders}를 기준으로 조립합니다.
 */
@Getter
@AllArgsConstructor
public class OwnerOrderSummaryDto {

	private Long id;

	private String email;

	private String address;

	private String zipCode;

	private LocalDateTime orderedAt;

	private Long totalAmount;

	private Long quantity;

	private OrderStatus orderStatus;

	private String coffeeName;

	public static OwnerOrderSummaryDto from(Orders orders) {
		Coffee coffee = orders.getCoffee();
		return new OwnerOrderSummaryDto(
			orders.getId(),
			orders.getEmail(),
			orders.getAddress(),
			orders.getZipCode(),
			orders.getOrderedAt(),
			orders.getTotalAmount(),
			orders.getQuantity(),
			orders.getOrderStatus(),
			coffee == null ? null : coffee.getName()
		);
	}
}
