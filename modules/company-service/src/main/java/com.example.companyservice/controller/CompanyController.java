package com.example.companyservice.controller;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public List<CompanyDto> getAllCompanies() {
        log.info("GET /companies called");
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id) {
        log.info("GET /companies/{} called", id);
        return companyService.getCompanyById(id);
    }

    @GetMapping("/name/{name}")
    public CompanyDto getCompanyByName(@PathVariable String name) {
        log.info("GET /companies/name/{} called", name);
        return companyService.getCompanyByName(name);
    }

    @PostMapping
    public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
        log.info("POST /companies called with body: {}", companyDto);
        return companyService.saveCompany(companyDto);
    }

    @GetMapping("/{id}/with-employees")
    public CompanyDto getCompanyWithEmployees(@PathVariable Long id) {
        log.info("GET /companies/{}/with-employees called", id);
        return companyService.getCompanyWithEmployees(id);
    }
}
