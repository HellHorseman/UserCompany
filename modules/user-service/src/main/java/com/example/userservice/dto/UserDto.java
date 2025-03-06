package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phoneNumber;
}

