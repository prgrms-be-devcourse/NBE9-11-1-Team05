package com.back.orderplz_01.global.initData;

import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.ApplicationRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final OrdersRepository ordersRepository;

    @Bean
    @Transactional
    public ApplicationRunner initData() {
        return applicationArguments -> {
            work1();
            orderStatusTest();
        };
    }

    public void work1() {
        // 필요 시 더미 데이터 추가
    }

    public void orderStatusTest() {

        Orders order = new Orders(
                "test@naver.com",
                "서울시 마포구",
                "03511",
                LocalDateTime.now(),
                10000L,
                OrderStatus.PROCESSING,
                new ArrayList<>()
        );

        ordersRepository.save(order);

        // 상태 변경 테스트
        order.changeStatus(OrderStatus.SHIPPED);
        order.changeStatus(OrderStatus.DELIVERED);
    }
}