package org.example.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userservice.Controlers.UserController;
import org.example.userservice.DTO.UserChangePhone;
import org.example.userservice.DTO.UserDto;
import org.example.userservice.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private UserService userService; // SpyBean umesto MockBean

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testFindAll() throws Exception {
        UserDto user1 = new UserDto("Aleksandar", "Golubovic", "alek@example.com", "123456789");
        UserDto user2 = new UserDto("Marko", "Markovic", "marko@example.com", "987654321");

        doReturn(List.of(user1, user2)).when(userService).findAll();

        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Aleksandar"))
                .andExpect(jsonPath("$[1].firstName").value("Marko"));
    }

    @Test
    void testAddUser() throws Exception {
        UserDto newUser = new UserDto("Petar", "Petrovic", "petar@example.com", "111222333");

        doReturn(ResponseEntity.ok("User added")).when(userService).addUser(any(UserDto.class));

        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User added"));
    }

    @Test
    void testDeleteUser() throws Exception {
        doReturn(ResponseEntity.ok("User deleted")).when(userService).deleteUser("alek@example.com");

        mockMvc.perform(delete("/users/delete/alek@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted"));
    }

    @Test
    void testUpdateUserPhone() throws Exception {
        UserChangePhone changePhone = new UserChangePhone("alek@example.com", "999888777");

        doReturn(ResponseEntity.ok("Phone updated")).when(userService).updateUser(any(UserChangePhone.class));

        mockMvc.perform(patch("/users/change-phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePhone)))
                .andExpect(status().isOk())
                .andExpect(content().string("Phone updated"));
    }

    @Test
    void testGetUserByEmail() throws Exception {
        doReturn(ResponseEntity.ok("User exists")).when(userService).findByEmail("alek@example.com");

        mockMvc.perform(get("/users/alek@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("User exists"));
    }

    @Test
    void testGetUserById() throws Exception {
        UserDto userDto = new UserDto("Aleksandar", "Golubovic", "alek@example.com", "123456789");

        doReturn(userDto).when(userService).findById(1);

        mockMvc.perform(get("/users/id/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Aleksandar"))
                .andExpect(jsonPath("$.lastName").value("Golubovic"))
                .andExpect(jsonPath("$.email").value("alek@example.com"))
                .andExpect(jsonPath("$.phone").value("123456789"));
    }
}
