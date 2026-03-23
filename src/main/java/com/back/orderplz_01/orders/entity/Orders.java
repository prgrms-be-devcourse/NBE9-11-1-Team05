package com.back.orderplz_01.orders.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.back.orderplz_01.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
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
	private Long totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	OrderStatus orderStatus;

	@OneToMany(mappedBy = "orders", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<OrdersItem> orderItems = new ArrayList<>();

	// public void addQuantityAndAmount(Long quantity, Long amount) {
	// 	this.quantity += quantity;
	// 	this.totalAmount += amount;
	// }
}
