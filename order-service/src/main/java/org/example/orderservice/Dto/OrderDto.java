package org.example.orderservice.Dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {
    private String useremail;
    private String productname;
    private Integer quantity;
}
