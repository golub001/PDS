package org.example.orderservice.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.CircutBreaker.Resiliance;
import org.example.orderservice.Dto.OrderDto;
import org.example.orderservice.Entity.Order;
import org.example.orderservice.Feign.UserFeign;
import org.example.orderservice.Repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final Resiliance userFeign;
    private final ProductService productService;
    private final ModelMapper modelMapper;
//    public List<OrderDto> getAllOrders() {
//        return orderRepository.findAll().stream().map(order -> {
//            OrderDto dto = new OrderDto();
//            dto.setId(order.getId());
//            dto.setQuantity(order.getQuantity());
//            dto.setUseremail(userFeign.getUserByEmail(order.get())); // koristi UserFeign
//            dto.setId(productRepository.getProductById(order.getProductId()));
//            return dto;
//        }).collect(Collectors.toList());
//    }

    public ResponseEntity<?> createOrder(OrderDto order) {
        Order saved = modelMapper.map(order, Order.class);
        ResponseEntity<?> response = userFeign.fetchUserByEmailOrThrow(order.getUseremail());
        if (response.hasBody() && response.getBody() instanceof Integer) {
            saved.setUserId((Integer) response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        ResponseEntity<?> response2 = productService.getbyName(order.getProductname());
        if (response.hasBody() && response.getBody() instanceof Integer) {
            saved.setProductId((Integer) response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        orderRepository.save(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
