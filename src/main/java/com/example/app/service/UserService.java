package com.example.app.service;

import com.example.app.dto.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();
    Optional<UserDto> getUserById(Long id);
    UserDto saveUser(UserDto userDto);
}
