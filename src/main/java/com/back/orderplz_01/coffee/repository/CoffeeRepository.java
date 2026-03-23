package com.back.orderplz_01.coffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.orderplz_01.coffee.entity.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {
}
