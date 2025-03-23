package com.example.companyservice.service.impl;

import com.example.companyservice.client.UserServiceClient;
import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.exception.CompanyNotFoundException;
import com.example.companyservice.exception.InternalServerErrorException;
import com.example.companyservice.exception.NoContentException;
import com.example.companyservice.mapper.CompanyMapper;
import com.example.companyservice.repository.CompanyRepository;
import com.example.companyservice.service.CompanyService;
import com.example.userservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserServiceClient userServiceClient;

    public List<CompanyDto> getAllCompanies() {
        List<CompanyDto> companies = companyRepository.findAll().stream()
                .map(companyMapper::toDto)
                .toList();
        if (companies.isEmpty()) {
            throw new NoContentException("No companies found");
        }
        log.info("Found {} companies", companies.size());
        return companies;
    }

    public CompanyDto getCompanyById(Long id) {
        CompanyDto company = companyRepository.findById(id)
                .map(companyMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Company with id {} not found", id);
                    return new CompanyNotFoundException("Company not found");
                });
        log.info("Company found: {}", company);
        return company;
    }

    public CompanyDto getCompanyByName(String name) {
        CompanyDto company = companyRepository.findByName(name)
                .map(companyMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Company with name {} not found", name);
                    return new CompanyNotFoundException("Company not found");
                });
        log.info("Company found: {}", company);
        return company;
    }

    @Transactional
    public CompanyDto saveCompany(CompanyDto companyDto) {
        try {
            var company = companyMapper.toEntity(companyDto);
            CompanyDto savedCompany = companyMapper.toDto(companyRepository.save(company));
            log.info("Company created successfully: {}", savedCompany);
            return savedCompany;
        } catch (Exception e) {
            log.error("Error while saving company: {}", e.getMessage());
            throw new InternalServerErrorException("Database error: " + e.getMessage());
        }
    }

    public CompanyDto getCompanyWithEmployees(Long companyId) {
        CompanyDto company = companyRepository.findById(companyId)
                .map(companyMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Company with id {} not found", companyId);
                    return new CompanyNotFoundException("Company not found");
                });

        List<Long> employeeIds = company.getEmployeeIds();
        log.info("Fetching employees for company {} with IDs: {}", companyId, employeeIds);

        List<UserDto> employees = userServiceClient.getUsersByIds(employeeIds);
        log.info("Fetched {} employees for company {}", employees.size(), companyId);

        company.setEmployees(employees);

        log.info("Company with employees: {}", company);
        return company;
    }
}
