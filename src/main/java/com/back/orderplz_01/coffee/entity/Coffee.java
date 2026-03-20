package com.back.orderplz_01.coffee.entity;

import com.back.orderplz_01.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
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

	@Column(nullable = false)
	private String imageUrl;

	@Column(nullable = false)
	private boolean soldOut;

	public void update(String name, String description, Long price, Long quantity, String imageUrl) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.imageUrl = imageUrl;
		this.soldOut = (quantity == null || quantity <= 0);
	}

}
