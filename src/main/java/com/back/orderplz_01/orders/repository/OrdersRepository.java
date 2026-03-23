package com.back.orderplz_01.orders.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.back.orderplz_01.orders.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
	@Query("""
		   SELECT o FROM Orders o
		   WHERE o.email = :email
		     AND o.address = :address
		     AND o.zipCode = :zipCode
		     AND o.orderStatus = 'PROCESSING'
		     AND o.orderedAt BETWEEN :start AND :end
		""")
	Optional<Orders> findBundleTarget(
		@Param("email") String email,
		@Param("address") String address,
		@Param("zipCode") String zipCode,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);
}
