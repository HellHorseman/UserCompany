package com.example.companyservice.service.impl;

import com.example.companyservice.client.UserServiceClient;
import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.mapper.CompanyMapper;
import com.example.companyservice.model.Company;
import com.example.companyservice.repository.CompanyRepository;
import com.example.commondto.UserDto;
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

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;
    private CompanyDto companyDto;

    @BeforeEach
    void setUp() {
        company = new Company(1L, "TestCorp", 100000.0, List.of(1L, 2L, 3L));
        companyDto = new CompanyDto(1L, "TestCorp", 100000.0, List.of(1L, 2L, 3L), null);
    }

    @Test
    void getAllCompanies_ShouldReturnList() {
        when(companyRepository.findAll()).thenReturn(List.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        List<CompanyDto> companies = companyService.getAllCompanies();

        assertFalse(companies.isEmpty());
        assertEquals(1, companies.size());
        assertEquals("TestCorp", companies.get(0).getName());

        verify(companyRepository, times(1)).findAll();
        verify(companyMapper, times(1)).toDto(company);
    }

    @Test
    void getCompanyById_ShouldReturnCompany_WhenExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.getCompanyById(1L);

        assertNotNull(result);
        assertEquals("TestCorp", result.getName());

        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void getCompanyByName_ShouldReturnCompany_WhenExists() {
        when(companyRepository.findByName("TestCorp")).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.getCompanyByName("TestCorp");

        assertNotNull(result);
        assertEquals(100000.0, result.getBudget());

        verify(companyRepository, times(1)).findByName("TestCorp");
    }

    @Test
    void saveCompany_ShouldReturnSavedCompany() {
        when(companyMapper.toEntity(companyDto)).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto savedCompany = companyService.saveCompany(companyDto);

        assertNotNull(savedCompany);
        assertEquals("TestCorp", savedCompany.getName());

        verify(companyRepository, times(1)).save(company);
    }

    @Test
    void getCompanyWithEmployees_ShouldReturnCompanyWithEmployees() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        List<UserDto> employees = List.of(
                new UserDto(1L, "John", "Doe", "1234567890"),
                new UserDto(2L, "Jane", "Smith", "0987654321")
        );
        when(userServiceClient.getUsersByIds(List.of(1L, 2L, 3L))).thenReturn(employees);

        CompanyDto result = companyService.getCompanyWithEmployees(1L);

        assertNotNull(result);
        assertEquals("TestCorp", result.getName());
        assertEquals(2, result.getEmployees().size());

        verify(companyRepository, times(1)).findById(1L);
        verify(userServiceClient, times(1)).getUsersByIds(List.of(1L, 2L, 3L));
    }
}