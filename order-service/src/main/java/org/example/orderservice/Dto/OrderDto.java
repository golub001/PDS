package org.example.orderservice.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {

    @NotBlank(message = "Email korisnika mora biti unet")
    @Email(message = "Email nije validan")
    private String useremail;

    @NotBlank(message = "Naziv proizvoda mora biti unet")
    private String productname;

    @NotNull(message = "Količina mora biti uneta")
    @Min(value = 1, message = "Količina mora biti najmanje 1")
    private Integer quantity;
}
