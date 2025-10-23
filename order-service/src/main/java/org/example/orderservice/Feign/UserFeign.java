package org.example.orderservice.Feign;

import org.example.orderservice.Dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeign {
    @GetMapping("/users/{email}")
    public ResponseEntity<?> GetId(@PathVariable("email") String email);

    @GetMapping("/users/id/{id}")
    public UserDto  GetById(@PathVariable("id") Integer id);
}
