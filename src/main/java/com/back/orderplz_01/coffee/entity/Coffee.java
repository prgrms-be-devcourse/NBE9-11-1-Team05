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
}
