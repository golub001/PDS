package org.example.userservice.Controlers;

import lombok.RequiredArgsConstructor;
import org.example.userservice.DTO.UserChangePhone;
import org.example.userservice.DTO.UserDto;
import org.example.userservice.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        return userService.deleteUser(email);
    }

    @PatchMapping("/change-phone")
    public ResponseEntity<?> updateUser(@RequestBody UserChangePhone newPhoneDto) {
        return userService.updateUser(newPhoneDto);
    }
}
