package com.back.orderplz_01.orders.dto;

import com.back.orderplz_01.orders.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderStatusChangeRequest {

    @NotNull
    private OrderStatus status;
}
