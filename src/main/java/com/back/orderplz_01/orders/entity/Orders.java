package com.back.orderplz_01.orders.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Orders extends BaseEntity {

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String zipCode;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime orderedAt;

	@Column(nullable = false)
	private Long totalAmount = 0L;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	OrderStatus orderStatus;

	@OneToMany(mappedBy = "orders", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<OrdersItem> orderItems = new ArrayList<>();

	public static Orders create(String email, String address, String zipCode) {
		Orders order = new Orders();
		order.email = email;
		order.address = address;
		order.zipCode = zipCode;
		order.orderStatus = OrderStatus.PROCESSING;
		order.totalAmount = 0L;
		return order;
	}

	public OrdersItem addOrderItem(Long quantity, Long price, Coffee coffee) {
		OrdersItem item = new OrdersItem(quantity, price, this, coffee);
		this.orderItems.add(item);
		this.totalAmount += price * quantity;
		return item;
	}

	public void addTotalAmount(Long amount) {
		this.totalAmount += amount;
	}

	// 배송 상태 변경
	public void changeStatus(OrderStatus newStatus) {

		if (this.orderStatus == OrderStatus.PROCESSING && newStatus == OrderStatus.SHIPPED) {
			this.orderStatus = newStatus;
			return;
		}

		if (this.orderStatus == OrderStatus.SHIPPED && newStatus == OrderStatus.DELIVERED) {
			this.orderStatus = newStatus;
			return;
		}

		if (this.orderStatus == OrderStatus.DELIVERED) {
			throw new IllegalStateException("배송 완료된 주문입니다.");
		}

		throw new IllegalStateException("잘못된 상태 변경입니다.");
	}

}
