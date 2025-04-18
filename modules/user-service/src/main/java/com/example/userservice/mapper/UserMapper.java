package com.example.userservice.mapper;

import com.example.commondto.userDtos.EmployeeDto;
import com.example.commondto.userDtos.UserRequestDto;
import com.example.commondto.userDtos.UserResponseDto;
import com.example.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companies", ignore = true)
    User toEntity(UserRequestDto requestDto);

    @Mapping(target = "companyCount", expression = "java(user.getCompanies().size())")
    UserResponseDto toResponseDto(User user);

    List<UserResponseDto> toResponseDtoList(List<User> users);

    // Для специальных случаев
    default EmployeeDto toSimpleDto(User user) {
        return EmployeeDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
