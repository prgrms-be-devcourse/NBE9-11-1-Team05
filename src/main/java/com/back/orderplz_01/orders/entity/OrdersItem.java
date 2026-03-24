package com.back.orderplz_01.orders.entity;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrdersItem extends BaseEntity {

	@Column(nullable = false)
	private Long quantity;

	@Column(nullable = false)
	private Long price;

	@ManyToOne
	@JoinColumn(name = "orders_id")
	private Orders orders;

	@ManyToOne
	@JoinColumn(name = "coffee_id")
	private Coffee coffee;

	public OrdersItem(Long quantity, Long price, Orders orders, Coffee coffee) {
		this.quantity = quantity;
		this.price = price;
		this.orders = orders;
		this.coffee = coffee;
	}

	public void addQuantity(Long quantity) {
		this.quantity += quantity;
	}
}
