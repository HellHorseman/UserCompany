package com.example.companyservice.service;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    public List<CompanyDto> getAllCompanies();

    public CompanyDto getCompanyById(Long id);

    public CompanyDto getCompanyByName(String name);

    public CompanyDto saveCompany(CompanyDto companyDto);

    public CompanyDto getCompanyWithEmployees(Long id);
}
