package com.back.orderplz_01.orders.service;

import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.repository.OrdersRepository;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;

    @Transactional
    public void changeStatus(Long orderId, OrderStatus newStatus){
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalIdentifierException("주문 없음"));

        validateStatusChange(order.getOrderStatus(), newStatus);

        order.changeStatus(newStatus);
    }

    private void validateStatusChange(OrderStatus current, OrderStatus next){
        if(current == OrderStatus.PROCESSING && next != OrderStatus.SHIPPED){
            throw new IllegalStateException(("PROCESSING -> SHIPPED만 가능"));
        }

        if(current == OrderStatus.SHIPPED && next != OrderStatus.DELIVERED){
            throw new IllegalStateException(("SHIPPED -> DELIVERED만 가능"));
        }

        if(current == OrderStatus.DELIVERED){
            throw new IllegalStateException(("이미 배송 완료된 주문"));
        }
    }

}
