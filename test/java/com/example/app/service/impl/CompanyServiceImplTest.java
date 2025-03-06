package com.example.app.service.impl;

import com.example.app.dto.CompanyDto;
import com.example.app.model.Company;
import com.example.app.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;
    private CompanyDto companyDto;

    @BeforeEach
    void setUp() {
        company = new Company(1L, "TestCorp", 100000.0, List.of(1L, 2L, 3L));
        companyDto = new CompanyDto("TestCorp", 100000.0, List.of(1L, 2L, 3L));
    }

    @Test
    void getAllCompanies_ShouldReturnList() {
        when(companyRepository.findAll()).thenReturn(List.of(company));

        List<CompanyDto> companies = companyService.getAllCompanies();

        assertFalse(companies.isEmpty());
        assertEquals(1, companies.size());
        assertEquals("TestCorp", companies.get(0).getName());

        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void getCompanyById_ShouldReturnCompany_WhenExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Optional<CompanyDto> result = companyService.getCompanyById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestCorp", result.get().getName());

        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void getCompanyById_ShouldReturnEmpty_WhenNotExists() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CompanyDto> result = companyService.getCompanyById(99L);

        assertTrue(result.isEmpty());

        verify(companyRepository, times(1)).findById(99L);
    }

    @Test
    void getCompanyByName_ShouldReturnCompany_WhenExists() {
        when(companyRepository.findByName("TestCorp")).thenReturn(Optional.of(company));

        Optional<CompanyDto> result = companyService.getCompanyByName("TestCorp");

        assertTrue(result.isPresent());
        assertEquals(100000.0, result.get().getBudget());

        verify(companyRepository, times(1)).findByName("TestCorp");
    }

    @Test
    void saveCompany_ShouldReturnSavedCompany() {
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyDto savedCompany = companyService.saveCompany(companyDto);

        assertNotNull(savedCompany);
        assertEquals("TestCorp", savedCompany.getName());

        verify(companyRepository, times(1)).save(any(Company.class));
    }
}
