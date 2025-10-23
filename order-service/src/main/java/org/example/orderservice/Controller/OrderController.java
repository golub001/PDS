package org.example.orderservice.Controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.Dto.OrderDto;
import org.example.orderservice.Entity.Order;
import org.example.orderservice.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/add")
    public ResponseEntity<?> addOrder(OrderDto orderDto)
    {
        return orderService.createOrder(orderDto);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getOrderDetails(@PathVariable Integer id){
        return orderService.getDetails(id);
    }
}
