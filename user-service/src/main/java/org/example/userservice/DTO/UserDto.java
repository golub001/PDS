package org.example.userservice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    @NotBlank(message = "Ime morate uneti")
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[A-Z].*",message = "Mora poceti velikim slovom")
    private String firstName;
    @NotBlank(message = "Prezime morate uneti")
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[A-Z].*",message = "Mora poceti velikim slovom")
    private String lastName;
    @NotBlank(message = "Email morate uneti")
    @Size(min = 3, max = 30)
    @Email
    private String email;
    @NotBlank(message = "Broj telefona morate uneti")
    @Size(max = 15)
    private String phone;
}
