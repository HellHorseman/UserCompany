package com.example.userservice.service;

import com.example.commondto.UserDto;
import com.example.commondto.userDtos.UserRequestDto;
import com.example.commondto.userDtos.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getUsersByIds(List<Long> ids);
    UserResponseDto createUser(UserRequestDto requestDto);

}
