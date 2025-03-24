package com.example.companyservice.service.impl;

import com.example.companyservice.client.UserServiceClient;
import com.example.commondto.CompanyDto;
import com.example.companyservice.mapper.CompanyMapper;
import com.example.companyservice.model.Company;
import com.example.companyservice.repository.CompanyRepository;
import com.example.commondto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private CompanyDto companyWithEmployeesDto;

    @BeforeEach
    void setUp() {
        company = Company.builder()
                .id(1L)
                .name("TestCorp")
                .budget(100000.0)
                .employeeIds(Set.of(1L, 2L, 3L))
                .build();

        companyDto = CompanyDto.builder()
                .id(1L)
                .name("TestCorp")
                .budget(100000.0)
                .employeeIds(Set.of(1L, 2L, 3L))
                .build();

        companyWithEmployeesDto = CompanyDto.builder()
                .id(1L)
                .name("TestCorp")
                .budget(100000.0)
                .employeeIds(Set.of(1L, 2L, 3L))
                .employees(List.of(
                        new UserDto(1L, "John", "Doe", "1234567890"),
                        new UserDto(2L, "Jane", "Smith", "0987654321")
                ))
                .build();
    }

    @Test
    void getAllCompanies_ShouldReturnList() {
        when(companyRepository.findAll()).thenReturn(List.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        List<CompanyDto> result = companyService.getAllCompanies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestCorp", result.get(0).getName());
        assertEquals(3, result.get(0).getEmployeeIds().size());

        verify(companyRepository).findAll();
        verify(companyMapper).toDto(company);
    }

    @Test
    void getCompanyById_ShouldReturnCompany_WhenExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.getCompanyById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Set.of(1L, 2L, 3L), result.getEmployeeIds());

        verify(companyRepository).findById(1L);
    }

    @Test
    void getCompanyByName_ShouldReturnCompany_WhenExists() {
        when(companyRepository.findByName("TestCorp")).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.getCompanyByName("TestCorp");

        assertNotNull(result);
        assertEquals("TestCorp", result.getName());
        assertTrue(result.getEmployeeIds().containsAll(Set.of(1L, 2L, 3L)));

        verify(companyRepository).findByName("TestCorp");
    }

    @Test
    void saveCompany_ShouldReturnSavedCompany() {
        when(companyMapper.toEntity(companyDto)).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.saveCompany(companyDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(3, result.getEmployeeIds().size());

        verify(companyRepository).save(company);
    }

    @Test
    void getCompanyWithEmployees_ShouldReturnCompanyWithEmployees() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        List<UserDto> mockEmployees = List.of(
                new UserDto(1L, "John", "Doe", "1234567890"),
                new UserDto(2L, "Jane", "Smith", "0987654321")
        );
        when(userServiceClient.getUsersByIds(anyList())).thenReturn(mockEmployees);

        CompanyDto result = companyService.getCompanyWithEmployees(1L);

        assertNotNull(result);
        assertEquals("TestCorp", result.getName());
        assertEquals(2, result.getEmployees().size());
        assertEquals("John", result.getEmployees().get(0).getFirstName());
        assertEquals("Jane", result.getEmployees().get(1).getFirstName());

        ArgumentCaptor<List<Long>> idsCaptor = ArgumentCaptor.forClass(List.class);
        verify(userServiceClient).getUsersByIds(idsCaptor.capture());

        List<Long> capturedIds = idsCaptor.getValue();
        assertEquals(3, capturedIds.size());
        assertTrue(capturedIds.containsAll(List.of(1L, 2L, 3L)));

        verify(companyRepository).findById(1L);
    }

    @Test
    void getCompanyWithEmployees_ShouldHandleEmptyEmployeeIds() {
        Company companyWithoutEmployees = Company.builder()
                .id(2L)
                .name("EmptyCorp")
                .employeeIds(Set.of())
                .build();

        CompanyDto dtoWithoutEmployees = CompanyDto.builder()
                .id(2L)
                .name("EmptyCorp")
                .employeeIds(Set.of())
                .build();

        when(companyRepository.findById(2L)).thenReturn(Optional.of(companyWithoutEmployees));
        when(companyMapper.toDto(companyWithoutEmployees)).thenReturn(dtoWithoutEmployees);

        CompanyDto result = companyService.getCompanyWithEmployees(2L);

        assertNotNull(result);
        assertTrue(result.getEmployeeIds().isEmpty());
        assertNotNull(result.getEmployees());
        assertTrue(result.getEmployees().isEmpty());
    }
}