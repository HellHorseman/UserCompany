package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();
    Optional<UserDto> getUserById(Long id);
    UserDto saveUser(UserDto userDto);
}
