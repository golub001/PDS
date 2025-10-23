package org.example.orderservice.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.CircutBreaker.Resiliance;
import org.example.orderservice.Dto.OrderDetails;
import org.example.orderservice.Dto.OrderDto;
import org.example.orderservice.Dto.ProductDto;
import org.example.orderservice.Dto.UserDto;
import org.example.orderservice.Entity.Order;
import org.example.orderservice.Feign.UserFeign;
import org.example.orderservice.Repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final Resiliance userFeign;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

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

    public ResponseEntity<?> getDetails(Integer id) {
        Optional<Order> opt = orderRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order with id " + id + " not found");
        }

        Order order = opt.get();

        UserDto userDto = null;
        ProductDto productDto = null;

        // 1) Pozovi User service preko Resiliance/Feign (već vraća UserDto)
        try {
            userDto = userFeign.fetchUserByIdOrThrow(order.getUserId());
        } catch (Exception ex) {
            log.warn("Unable to fetch user for order id={} userId={} -> {}",
                    id, order.getUserId(), ex.toString());
            // fallback DTO ako želiš
            userDto = new UserDto();
            userDto.setFirstName("Unknown User (CB fallback)");
            userDto.setLastName("Unknown User (CB fallback)");
            userDto.setEmail("unknown@example.com");
        }

        // 2) Pozovi Product service
        try {
            ResponseEntity<?> productResp = productService.getOne(order.getProductId());
            if (productResp != null && productResp.hasBody() && productResp.getBody() instanceof ProductDto) {
                productDto = (ProductDto) productResp.getBody();
            }
        } catch (Exception ex) {
            log.warn("Unable to fetch product for order id={} productId={} -> {}",
                    id, order.getProductId(), ex.toString());
        }

        // 3) Sastavi OrderDetails
        OrderDetails details = new OrderDetails();
        details.setId(order.getId());
        details.setQuantity(order.getQuantity());
        details.setUserId(order.getUserId());
        details.setProductId(order.getProductId());
        details.setUser(userDto);
        details.setProduct(productDto);
        Float asd=productDto.getPrice()*order.getQuantity();
        details.setFullprice(asd);
        return ResponseEntity.ok(details);
    }
}
