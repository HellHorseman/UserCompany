package com.example.commondto.companyDtos;

import com.example.commondto.userDtos.UserResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyWithEmployeesDto {
    private CompanyResponseDto company;
    private List<UserResponseDto> employees;
}
