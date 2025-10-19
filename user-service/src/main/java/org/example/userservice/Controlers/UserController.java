package org.example.userservice.Controlers;

import org.example.userservice.DTO.UserDto;
import org.example.userservice.Entity.User;



import org.example.userservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<UserDto> findAll() {
        List<User> list= userRepository.findAll();
        return list.stream().map(x->modelMapper.map(x,UserDto.class)).toList();
    }
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        User user=modelMapper.map(userDto,User.class);
        if((userRepository.existsByEmail(userDto.getEmail()))==false) {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        else  {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("That email already exists");
        }
    }
    @Transactional
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        if(userRepository.existsByEmail(email) && (userRepository.deleteByEmail(email))>0){
            return ResponseEntity.status(HttpStatus.OK).body("User deleted");
        }
        else  {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }
}
