package com.back.orderplz_01.coffee.entity;

import com.back.orderplz_01.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Coffee extends BaseEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Long price;

	@Column(nullable = false)
	private Long quantity;

	public static Coffee create(String name, String description, Long price, Long quantity) {
		Coffee coffee = new Coffee();
		coffee.name = name;
		coffee.description = description;
		coffee.price = price;
		coffee.quantity = quantity;
		return coffee;
	}

	public void decreaseQuantity(Long quantity) {
		if (this.quantity < quantity) {
			throw new IllegalArgumentException("재고가 부족합니다.");
		}
		this.quantity -= quantity;
	}

	public void update(String name, String description, Long price, Long quantity) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;

	}

}
