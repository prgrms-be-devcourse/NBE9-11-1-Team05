package com.back.orderplz_01.orders.controller;

import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "OrdersController", description = "주문 API")
public class OrdersController {

    private final OrdersService ordersService;

    @PatchMapping("/{id}/status")
    public void changeStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ){
        ordersService.changeStatus(id, status);
    }

}
