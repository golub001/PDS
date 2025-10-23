package org.example.orderservice.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetails {
    private Integer id;
    private Integer quantity;
    private Integer userId;
    private Integer productId;

    private UserDto user;
    private ProductDto product;
    private Float fullprice;
}
