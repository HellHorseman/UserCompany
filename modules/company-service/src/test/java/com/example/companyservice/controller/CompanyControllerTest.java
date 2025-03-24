package com.example.companyservice.controller;

import com.example.commondto.CompanyDto;
import com.example.companyservice.exception.CompanyNotFoundException;
import com.example.companyservice.service.CompanyService;
import com.example.commondto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @Autowired
    private ObjectMapper objectMapper;

    private CompanyDto companyDto;
    private CompanyDto companyWithEmployeesDto;

    @BeforeEach
    void setUp() {
        companyDto = CompanyDto.builder()
                .id(1L)
                .name("TestCorp")
                .budget(100000.0)
                .employeeIds(Set.of(1L, 2L))
                .build();

        companyWithEmployeesDto = CompanyDto.builder()
                .id(1L)
                .name("TestCorp")
                .budget(100000.0)
                .employeeIds(Set.of(1L, 2L))
                .employees(List.of(
                        new UserDto(1L, "John", "Doe", "1234567890"),
                        new UserDto(2L, "Jane", "Smith", "0987654321")
                ))
                .build();
    }

    @Test
    void getAllCompanies_ShouldReturnList() throws Exception {
        when(companyService.getAllCompanies()).thenReturn(List.of(companyDto));

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("TestCorp"))
                .andExpect(jsonPath("$[0].budget").value(100000.0))
                .andExpect(jsonPath("$[0].employeeIds.length()").value(2));

        verify(companyService, times(1)).getAllCompanies();
    }

    @Test
    void getCompanyWithEmployees_ShouldReturnCompanyWithEmployees() throws Exception {
        when(companyService.getCompanyWithEmployees(1L)).thenReturn(companyWithEmployeesDto);

        mockMvc.perform(get("/companies/1/with-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestCorp"))
                .andExpect(jsonPath("$.budget").value(100000.0))
                .andExpect(jsonPath("$.employeeIds.length()").value(2)) // Для Set
                .andExpect(jsonPath("$.employees.size()").value(2))
                .andExpect(jsonPath("$.employees[0].firstName").value("John"))
                .andExpect(jsonPath("$.employees[1].firstName").value("Jane"));

        verify(companyService, times(1)).getCompanyWithEmployees(1L);
    }

    @Test
    void getCompanyWithEmployees_ShouldReturnNotFound_WhenCompanyNotExists() throws Exception {
        when(companyService.getCompanyWithEmployees(99L))
                .thenThrow(new CompanyNotFoundException("Company not found with id: 99"));

        mockMvc.perform(get("/companies/99/with-employees"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Company not found with id: 99"));

        verify(companyService, times(1)).getCompanyWithEmployees(99L);
    }

    @Test
    void getCompanyWithEmployees_ShouldReturnEmptyEmployees_WhenNoEmployees() throws Exception {
        CompanyDto companyWithoutEmployees = CompanyDto.builder()
                .id(1L)
                .name("TestCorp")
                .budget(100000.0)
                .employeeIds(Set.of())
                .employees(List.of())
                .build();

        when(companyService.getCompanyWithEmployees(1L)).thenReturn(companyWithoutEmployees);

        mockMvc.perform(get("/companies/1/with-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeIds.length()").value(0))
                .andExpect(jsonPath("$.employees.size()").value(0));

        verify(companyService, times(1)).getCompanyWithEmployees(1L);
    }
}
