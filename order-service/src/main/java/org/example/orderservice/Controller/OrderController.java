package org.example.orderservice.Controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.Dto.OrderDto;
import org.example.orderservice.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("add")
    public ResponseEntity<?> addOrder(OrderDto orderDto)
    {
        return orderService.createOrder(orderDto);
    }
}
