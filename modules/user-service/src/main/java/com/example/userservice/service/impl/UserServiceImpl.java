package com.example.userservice.service.impl;

import com.example.commondto.userDtos.UserRequestDto;
import com.example.commondto.userDtos.UserResponseDto;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        log.info("Found {} users on page {}", users.getNumberOfElements(), pageable.getPageNumber());
        return users.map(userMapper::toResponseDto);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with id {} not found", id);
                    return new UserNotFoundException("User not found");
                });
        log.info("User found with id: {}", id);
        return userMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getUsersByIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        log.info("Found {} users by ids: {}", users.size(), ids);
        return userMapper.toResponseDtoList(users);
    }

    @Transactional
    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user = userMapper.toEntity(requestDto);
        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        return userMapper.toResponseDto(savedUser);
    }
}
