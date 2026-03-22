package com.back.orderplz_01.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.back.orderplz_01.orders.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

	@EntityGraph(attributePaths = { "coffee" })
	List<Orders> findAllByOrderByOrderedAtDesc();

	@EntityGraph(attributePaths = { "coffee" })
	List<Orders> findByEmailIgnoreCaseOrderByOrderedAtDesc(String email);

	@EntityGraph(attributePaths = { "coffee" })
	List<Orders> findByEmailIgnoreCaseAndZipCodeIgnoreCaseOrderByOrderedAtDesc(String email, String zipCode);
}
