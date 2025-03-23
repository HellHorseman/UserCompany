package com.example.companyservice.dto;

import com.example.commondto.UserDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {
    private Long id;
    private String name;
    private Double budget;
    private List<Long> employeeIds;
    private List<UserDto> employees;
}
