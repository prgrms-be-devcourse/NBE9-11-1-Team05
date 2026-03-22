package com.back.orderplz_01.orders.dto;

import com.back.orderplz_01.orders.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusUpdateRequest {
    private OrderStatus status;
}
