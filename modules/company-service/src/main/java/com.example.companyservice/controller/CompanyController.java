package com.example.companyservice.controller;

import com.example.commondto.companyDtos.CompanyRequestDto;
import com.example.commondto.companyDtos.CompanyResponseDto;
import com.example.commondto.userDtos.EmployeeDto;
import com.example.companyservice.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
@Validated
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public Page<CompanyResponseDto> getAllCompanies(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /companies called with pageable: {}", pageable);
        return companyService.getAllCompanies(pageable);
    }

    @GetMapping("/{id}")
    public CompanyResponseDto getCompanyById(@PathVariable Long id) {
        log.info("GET /companies/{} called", id);
        return companyService.getCompanyById(id);
    }

    @GetMapping("/name/{name}")
    public CompanyResponseDto getCompanyByName(@PathVariable String name) {
        log.info("GET /companies/name/{} called", name);
        return companyService.getCompanyByName(name);
    }

    @PostMapping
    public CompanyResponseDto createCompany(
            @Valid @RequestBody CompanyRequestDto companyRequestDto) {
        log.info("POST /companies called with body: {}", companyRequestDto);
        return companyService.createCompany(companyRequestDto);
    }

    @GetMapping("/{id}/employees")
    public List<EmployeeDto> getCompanyEmployees(@PathVariable Long id) {
        log.info("GET /companies/{}/employees called", id);
        return companyService.getCompanyEmployees(id);
    }
}
