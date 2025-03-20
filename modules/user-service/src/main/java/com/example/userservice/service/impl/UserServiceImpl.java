package com.example.userservice.service.impl;

import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
        log.info("Found {} users", users.size());
        return users;
    }

    public UserDto getUserById(Long id) {
        UserDto user = userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("User with id {} not found", id);
                    return new RuntimeException("User not found");
                });
        log.info("User found: {}", user);
        return user;
    }

    public List<UserDto> getUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids).stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber()))
                .toList();
    }

    @Transactional
    public UserDto saveUser(UserDto userDto) {
        var user = userMapper.toEntity(userDto);
        UserDto savedUser = userMapper.toDto(userRepository.save(user));
        log.info("User created successfully: {}", savedUser);
        return savedUser;
    }
}
