package com.example.userservice.controller;

import com.example.commondto.userDtos.UserRequestDto;
import com.example.commondto.userDtos.UserResponseDto;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public Page<UserResponseDto> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /users called with pageable: {}", pageable);
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        log.info("GET /users/{} called", id);
        return userService.getUserById(id);
    }

    @PostMapping("/by-ids")
    public List<UserResponseDto> getUsersByIds(
            @Valid @RequestBody List<Long> ids) {
        log.info("POST /users/by-ids called with ids: {}", ids);
        return userService.getUsersByIds(ids);
    }

    @PostMapping
    public UserResponseDto createUser(
            @Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("POST /users called with body: {}", userRequestDto);
        return userService.createUser(userRequestDto);
    }
}
