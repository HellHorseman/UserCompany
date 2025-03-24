package com.example.companyservice.service;

import com.example.commondto.CompanyDto;

import java.util.List;

public interface CompanyService {

    List<CompanyDto> getAllCompanies();

    CompanyDto getCompanyById(Long id);

    CompanyDto getCompanyByName(String name);

    CompanyDto saveCompany(CompanyDto companyDto);

    CompanyDto getCompanyWithEmployees(Long id);
}
