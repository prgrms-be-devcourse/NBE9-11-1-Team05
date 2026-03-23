package com.back.orderplz_01.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.orderplz_01.orders.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
