package org.example.userservice.Service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.DTO.UserChangePhone;
import org.example.userservice.DTO.UserDto;
import org.example.userservice.Entity.User;
import org.example.userservice.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> findAll() {
        List<User> list = userRepository.findAll();
        return list.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
    public ResponseEntity<?> findByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Daj korisnik ne postoji");
        }
        Optional<?> id=userRepository.getUserIdByEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
    public ResponseEntity<?> addUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("That email already exists");
        }

        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Transactional
    public ResponseEntity<?> deleteUser(String email) {
        if (userRepository.existsByEmail(email) && userRepository.deleteByEmail(email) > 0) {
            return ResponseEntity.status(HttpStatus.OK).body("User deleted");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
    }

    public ResponseEntity<?> updateUser(UserChangePhone newPhoneDto) {
        User user = (User) userRepository.getUsersByEmail(newPhoneDto.getEmail())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        user.setPhone(newPhoneDto.getPhone());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public UserDto findById(Integer id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDto.class))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with ID " + id + " not found"
                ));
    }
}
