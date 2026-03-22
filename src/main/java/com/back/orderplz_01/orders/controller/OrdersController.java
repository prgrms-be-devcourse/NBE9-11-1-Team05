package com.back.orderplz_01.orders.controller;

import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "OrdersController", description = "주문 API")
public class OrdersController {

    private final OrdersService ordersService;

    @Operation(
            summary = "주문 상태 변경",
            description = "PROCESSING → SHIPPED → DELIVERED 순으로 상태 변경"
    )
    @PatchMapping("/{id}/status")
    public void changeStatus(
            @Parameter(description = "주문 ID")
            @PathVariable Long id,
            @Parameter(description = "주문 상태 (PROCESSING, SHIPPED, DELIVERED)")
            @RequestParam OrderStatus status
    ){
        ordersService.changeStatus(id, status);
    }

}
