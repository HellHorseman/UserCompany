package com.example.companyservice.service.impl;

import com.example.commondto.companyDtos.CompanyRequestDto;
import com.example.commondto.companyDtos.CompanyResponseDto;
import com.example.commondto.companyDtos.CompanyWithEmployeesDto;
import com.example.commondto.userDtos.UserResponseDto;
import com.example.companyservice.client.UserServiceClient;
import com.example.companyservice.exception.CompanyNotFoundException;
import com.example.companyservice.exception.NoContentException;
import com.example.companyservice.mapper.CompanyMapper;
import com.example.companyservice.model.Company;
import com.example.companyservice.repository.CompanyRepository;
import com.example.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public Page<CompanyResponseDto> getAllCompanies(Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(pageable);
        log.info("Found {} companies on page {}", companies.getNumberOfElements(), pageable.getPageNumber());
        return companies.map(companyMapper::toResponseDto);
    }

    @Override
    public CompanyResponseDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Company with id {} not found", id);
                    return new CompanyNotFoundException("Company not found");
                });
        log.info("Company found with id: {}", id);
        return companyMapper.toResponseDto(company);
    }

    @Override
    public CompanyResponseDto getCompanyByName(String name) {
        Company company = companyRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn("Company with name {} not found", name);
                    return new CompanyNotFoundException("Company not found");
                });
        log.info("Company found with name: {}", name);
        return companyMapper.toResponseDto(company);
    }

    @Transactional
    @Override
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) {
        Company company = companyMapper.toEntity(requestDto);
        Company savedCompany = companyRepository.save(company);
        log.info("Company created successfully with id: {}", savedCompany.getId());
        return companyMapper.toResponseDto(savedCompany);
    }

    @Override
    public CompanyWithEmployeesDto getCompanyWithEmployees(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> {
                    log.warn("Company with id {} not found", companyId);
                    return new CompanyNotFoundException("Company not found");
                });

        return companyMapper.toCompanyWithEmployeesDto(
                company,
                fetchEmployeesForCompany(company)
        );
    }

    private List<UserResponseDto> fetchEmployeesForCompany(Company company) {
        if (company.getEmployees() == null || company.getEmployees().isEmpty()) {
            log.info("Company {} has no employees", company.getId());
            return Collections.emptyList();
        }

        List<Long> employeeIds = company.getEmployees().stream()
                .map(User::getId)
                .toList();

        log.info("Fetching employees for company {} with IDs: {}", company.getId(), employeeIds);
        return userServiceClient.getUsersByIds(employeeIds);
    }
}
