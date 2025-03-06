package com.example.companyservice.service.impl;

import com.example.app.dto.CompanyDto;
import com.example.app.model.Company;
import com.example.companyservice.repository.CompanyRepository;
import com.example.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CompanyDto> getCompanyById(Long id) {
        return companyRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public Optional<CompanyDto> getCompanyByName(String name) {
        return companyRepository.findByName(name)
                .map(this::convertToDto);
    }

    @Override
    public CompanyDto saveCompany(CompanyDto companyDto) {
        Company company = convertToEntity(companyDto);
        Company savedCompany = companyRepository.save(company);
        return convertToDto(savedCompany);
    }

    private CompanyDto convertToDto(Company company) {
        return new CompanyDto(company.getName(), company.getBudget(), company.getEmployees());
    }

    private Company convertToEntity(CompanyDto companyDto) {
        return Company.builder()
                .name(companyDto.getName())
                .budget(companyDto.getBudget())
                .employees(companyDto.getEmployees())
                .build();
    }
}
