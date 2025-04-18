package com.example.companyservice.service;

import com.example.commondto.companyDtos.CompanyRequestDto;
import com.example.commondto.companyDtos.CompanyResponseDto;
import com.example.commondto.companyDtos.CompanyWithEmployeesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {

    Page<CompanyResponseDto> getAllCompanies(Pageable pageable);

    CompanyResponseDto getCompanyById(Long id);

    CompanyResponseDto getCompanyByName(String name);

    CompanyResponseDto createCompany(CompanyRequestDto requestDto);

    CompanyWithEmployeesDto getCompanyWithEmployees(Long companyId);
}
