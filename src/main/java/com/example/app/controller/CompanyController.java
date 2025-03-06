package com.example.app.controller;

import com.example.app.dto.CompanyDto;
import com.example.app.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            log.info("List of companies is empty.");
            return ResponseEntity.noContent().build();
        }
        log.info("Found {} companies", companies.size());
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Company with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CompanyDto> getCompanyByName(@PathVariable String name) {
        return companyService.getCompanyByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Company with name {} not found", name);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto) {
        try {
            CompanyDto savedCompany = companyService.saveCompany(companyDto);
            return ResponseEntity.ok(savedCompany);
        } catch (Exception e) {
            log.error("Error while saving company: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
