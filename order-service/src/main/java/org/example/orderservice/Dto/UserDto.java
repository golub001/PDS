package org.example.orderservice.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    @NotBlank(message = "Email korisnika mora biti unet")
    @Email(message = "Email nije validan")
    private String email;

    @NotBlank(message = "Ime mora biti uneto")
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[A-Z].*", message = "Ime mora početi velikim slovom")
    private String firstName;

    @NotBlank(message = "Prezime mora biti uneto")
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[A-Z].*", message = "Prezime mora početi velikim slovom")
    private String lastName;

    @NotBlank(message = "Broj telefona mora biti unet")
    @Size(max = 15)
    private String phone;
}
