package com.example.app.service.impl;

import com.example.app.dto.UserDto;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .build();

        userDto = new UserDto("John", "Doe", "1234567890");
    }

    @Test
    void shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0)).isEqualTo(userDto);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDto> foundUser = userService.getUserById(1L);

        assertThat(foundUser).isPresent().contains(userDto);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUser = userService.saveUser(userDto);

        assertThat(savedUser).isEqualTo(userDto);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
