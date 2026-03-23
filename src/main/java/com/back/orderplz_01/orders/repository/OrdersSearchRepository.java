package com.back.orderplz_01.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.back.orderplz_01.orders.entity.Orders;

public interface OrdersSearchRepository extends JpaRepository<Orders, Long> {

	/** CUS-09 :: 주문 들어온 목록 조회 쿼리 */
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

