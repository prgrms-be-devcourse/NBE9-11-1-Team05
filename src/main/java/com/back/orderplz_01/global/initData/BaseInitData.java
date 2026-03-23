package com.back.orderplz_01.global.initData;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner initData() {
        return args -> {
            self.work1();
            self.coffeeStatusTest();
        };
    }

    @Transactional
    public void work1() {

    }

    @Transactional
    public void coffeeStatusTest() {

        Coffee coffee = new Coffee(
                "콜롬비아 원두",
                "고소한 맛",
                1000L,
                10L
        );

        coffeeRepository.save(coffee);

        Orders order = new Orders(
                "test@naver.com",
                "서울시 마포구",
                "03511",
                LocalDateTime.now(),
                10000L,
                1L,
                OrderStatus.PROCESSING,
                coffee
        );

        ordersRepository.save(order);
    }
}
