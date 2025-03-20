package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    List<UserDto> getUsersByIds(List<Long> ids);
    UserDto saveUser(UserDto userDto);

}
