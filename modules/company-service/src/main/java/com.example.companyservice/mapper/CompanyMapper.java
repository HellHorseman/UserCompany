package com.example.companyservice.mapper;

import com.example.commondto.companyDtos.CompanyRequestDto;
import com.example.commondto.companyDtos.CompanyResponseDto;
import com.example.commondto.companyDtos.CompanyWithEmployeesDto;
import com.example.commondto.userDtos.UserResponseDto;
import com.example.companyservice.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Company toEntity(CompanyRequestDto requestDto);

    @Mapping(target = "employeeCount", expression = "java(company.getEmployees().size())")
    CompanyResponseDto toResponseDto(Company company);

    List<CompanyResponseDto> toResponseDtoList(List<Company> companies);

    default CompanyWithEmployeesDto toCompanyWithEmployeesDto(
            Company company,
            List<UserResponseDto> employees) {
        return CompanyWithEmployeesDto.builder()
                .company(toResponseDto(company))
                .employees(employees)
                .build();
    }
}
