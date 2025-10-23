package org.example.userservice.Service;

import org.example.userservice.DTO.UserChangePhone;
import org.example.userservice.DTO.UserDto;
import org.example.userservice.Entity.User;
import org.example.userservice.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Koristi MockitoExtension za inicijalizaciju mock-ova
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // Kreira instancu UserService i ubacuje mock-ove u nju
    @InjectMocks
    private UserService userService;

    // Kreira mock objekte za zavisnosti
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    // Test podaci
    private User userEntity;
    private UserDto userDto;
    private UserChangePhone userChangePhoneDto;
    private final String TEST_EMAIL = "test@example.com";
    private final Integer TEST_ID = 1;

    // Postavljanje početnih test podataka pre svakog testa
    @BeforeEach
    void setUp() {
        // AŽURIRANO: Koristi 5 argumenata za User entitet (ID, firstName, lastName, email, phone)
        userEntity = new User(TEST_ID, "Test", "Ime", TEST_EMAIL, "123456789");

        // Pretpostavljamo da UserDto sada takođe ima 4 argumenta (firstName, lastName, email, phone)
        userDto = new UserDto("Test", "Ime", TEST_EMAIL, "123456789");

        userChangePhoneDto = new UserChangePhone(TEST_EMAIL, "987654321");
    }

    // --- findAll Test ---
    @Test
    void findAll_shouldReturnListOfUserDtos() {
        // Arrange
        List<User> userList = Arrays.asList(userEntity);
        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(userDto);

        // Act
        List<UserDto> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(User.class), eq(UserDto.class));
    }

    // --- findByEmail Test ---
    @Test
    void findByEmail_shouldReturnUserId_whenUserExists() {
        // Arrange
        // Korišćenje Optional<?> za kreiranje objekta
        Optional<?> userIdOptional = Optional.of(TEST_ID);

        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

        // FIX: Koristimo sirovi Optional (casting) da izbegnemo grešku u Mockito tipovima
        when(userRepository.getUserIdByEmail(TEST_EMAIL)).thenReturn((Optional) userIdOptional);

        // Act
        ResponseEntity<?> response = userService.findByEmail(TEST_EMAIL);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userIdOptional, response.getBody());
        verify(userRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(userRepository, times(1)).getUserIdByEmail(TEST_EMAIL);
    }

    @Test
    void findByEmail_shouldReturnBadRequest_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);

        // Act
        ResponseEntity<?> response = userService.findByEmail(TEST_EMAIL);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Daj korisnik ne postoji", response.getBody());
        verify(userRepository, times(1)).existsByEmail(TEST_EMAIL);
    }

    // --- addUser Test ---
    @Test
    void addUser_shouldAddUser_whenEmailIsUnique() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        when(modelMapper.map(userDto, User.class)).thenReturn(userEntity);

        // Act
        ResponseEntity<?> response = userService.addUser(userDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userEntity, response.getBody());
        verify(userRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void addUser_shouldReturnBadRequest_whenEmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

        // Act
        ResponseEntity<?> response = userService.addUser(userDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("That email already exists", response.getBody());
        verify(userRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(userRepository, never()).save(any(User.class));
    }

    // --- deleteUser Test ---
    @Test
    void deleteUser_shouldDeleteUser_whenUserExists() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);
        when(userRepository.deleteByEmail(TEST_EMAIL)).thenReturn(1);

        // Act
        ResponseEntity<?> response = userService.deleteUser(TEST_EMAIL);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted", response.getBody());
        verify(userRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(userRepository, times(1)).deleteByEmail(TEST_EMAIL);
    }

    @Test
    void deleteUser_shouldReturnBadRequest_whenUserNotFound() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);

        // Act
        ResponseEntity<?> response = userService.deleteUser(TEST_EMAIL);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(userRepository, never()).deleteByEmail(anyString());
    }

    // --- updateUser Test ---
    @Test
    void updateUser_shouldUpdatePhone_whenUserExists() {
        // Arrange
        // FIX: Korišćenje doReturn().when() sintakse za sigurno stubovanje Optional<User>
        doReturn(Optional.of(userEntity))
                .when(userRepository).getUsersByEmail(TEST_EMAIL);

        // Act
        ResponseEntity<?> response = userService.updateUser(userChangePhoneDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User updatedUser = (User) response.getBody();
        assertNotNull(updatedUser);
        assertEquals(userChangePhoneDto.getPhone(), updatedUser.getPhone());
        verify(userRepository, times(1)).getUsersByEmail(TEST_EMAIL);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void updateUser_shouldReturnBadRequest_whenUserNotFound() {
        // Arrange
        when(userRepository.getUsersByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userService.updateUser(userChangePhoneDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userRepository, times(1)).getUsersByEmail(TEST_EMAIL);
        verify(userRepository, never()).save(any(User.class));
    }

    // --- findById Test ---
    @Test
    void findById_shouldReturnUserDto_whenUserExists() {
        // Arrange
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(userDto);

        // Act
        UserDto result = userService.findById(TEST_ID);

        // Assert
        assertNotNull(result);
        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(TEST_ID);
        verify(modelMapper, times(1)).map(userEntity, UserDto.class);
    }

    @Test
    void findById_shouldThrowResponseStatusException_whenUserNotFound() {
        // Arrange
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.findById(TEST_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("not found"));
        verify(userRepository, times(1)).findById(TEST_ID);
        verify(modelMapper, never()).map(any(), any());
    }
}