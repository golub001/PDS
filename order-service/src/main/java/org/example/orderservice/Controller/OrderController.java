package org.example.orderservice.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.Dto.OrderDto;
import org.example.orderservice.Entity.Order;
import org.example.orderservice.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@Valid @RequestBody OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getOrderDetails(@PathVariable Integer id) {
        return orderService.getDetails(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
        return orderService.deleteOrder(id);
    }
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<?> updateOrderQuantity(@PathVariable Integer id, @RequestParam Integer quantity) {
        return orderService.updateOrderQuantity(id, quantity);
    }

}
