package com.example.order.controller.order;

import com.example.order.controller.order.dto.OrderCreateRequest;
import com.example.order.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/create")
    public Integer createOrder(@Validated @RequestBody OrderCreateRequest request){
        return orderService.createOrder(request);
    }

}
