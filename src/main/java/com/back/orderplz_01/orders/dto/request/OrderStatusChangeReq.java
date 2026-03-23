package com.back.orderplz_01.orders.dto.request;

import com.back.orderplz_01.orders.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusChangeReq (

        @NotNull
        OrderStatus status
){
}

