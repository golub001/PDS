package org.example.orderservice.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {

    @NotBlank(message = "Morate uneti ime")
    private String name;

    private String description;

    @NotNull(message = "Morate uneti cenu")
    private Float price;
}
