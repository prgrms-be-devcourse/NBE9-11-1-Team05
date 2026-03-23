package com.back.orderplz_01.global.initData;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Profile("test")
public class TestInitData {

    private final CoffeeRepository coffeeRepository;
    private final OrdersRepository ordersRepository;

    @Bean
    public ApplicationRunner initTestData() {
        return args -> init();
    }

    @Transactional
    public void init() {

        Coffee coffee = new Coffee(
                "테스트 원두",
                "테스트용 설명",
                1000L,
                10L
        );

        coffeeRepository.save(coffee);

        Orders order = new Orders(
                "test@naver.com",
                "서울시 마포구",
                "00000",
                LocalDateTime.now(),
                10000L,
                1L,
                OrderStatus.PROCESSING,
                coffee
        );

        ordersRepository.save(order);
    }
}