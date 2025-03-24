package com.example.commondto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {
    private Long id;
    private String name;
    private Double budget;
    private String description;
    private Set<Long> employeeIds;
    private List<UserDto> employees;
}
