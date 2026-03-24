package com.back.orderplz_01.orders.repository;

import java.time.LocalDateTime;
import java.util.List;
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

	/* CUS-09 내 주문 목록 검색. (이메일 배송지 주소 우편번호) */
	@Query("""
		SELECT DISTINCT o
		FROM Orders o
		LEFT JOIN FETCH o.orderItems oi
		LEFT JOIN FETCH oi.coffee
		WHERE o.email = :email
		  AND o.address = :address
		  AND o.zipCode = :zipCode
		ORDER BY o.createDate DESC
		""")
	List<Orders> findOrdersForList(
		@Param("email") String email,
		@Param("address") String address,
		@Param("zipCode") String zipCode
	);
}
