package com.example.companyservice.controller;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.exception.CompanyNotFoundException;
import com.example.companyservice.exception.InternalServerErrorException;
import com.example.companyservice.exception.NoContentException;
import com.example.companyservice.service.CompanyService;
import com.example.userservice.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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

    @BeforeEach
    void setUp() {
        companyDto = CompanyDto.builder()
                .id(1L)
                .name("TestCorp")
                .budget(100000.0)
                .employeeIds(List.of(1L, 2L))
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
                .andExpect(jsonPath("$[0].employeeIds.size()").value(2));

        verify(companyService, times(1)).getAllCompanies();
    }

    @Test
    void getAllCompanies_ShouldReturnNoContent_WhenEmpty() throws Exception {
        when(companyService.getAllCompanies()).thenThrow(new NoContentException("No companies found"));

        mockMvc.perform(get("/companies"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No companies found"));

        verify(companyService, times(1)).getAllCompanies();
    }

    @Test
    void getCompanyById_ShouldReturnCompany_WhenExists() throws Exception {
        when(companyService.getCompanyById(1L)).thenReturn(companyDto);

        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestCorp"))
                .andExpect(jsonPath("$.budget").value(100000.0))
                .andExpect(jsonPath("$.employeeIds.size()").value(2));

        verify(companyService, times(1)).getCompanyById(1L);
    }

    @Test
    void getCompanyById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(companyService.getCompanyById(99L)).thenThrow(new CompanyNotFoundException("Company not found with id: 99"));

        mockMvc.perform(get("/companies/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Company not found with id: 99"));

        verify(companyService, times(1)).getCompanyById(99L);
    }

    @Test
    void getCompanyByName_ShouldReturnCompany_WhenExists() throws Exception {
        when(companyService.getCompanyByName("TestCorp")).thenReturn(companyDto);

        mockMvc.perform(get("/companies/name/TestCorp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestCorp"))
                .andExpect(jsonPath("$.budget").value(100000.0))
                .andExpect(jsonPath("$.employeeIds.size()").value(2));

        verify(companyService, times(1)).getCompanyByName("TestCorp");
    }

    @Test
    void getCompanyByName_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(companyService.getCompanyByName("Unknown")).thenThrow(new CompanyNotFoundException("Company not found with name: Unknown"));

        mockMvc.perform(get("/companies/name/Unknown"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Company not found with name: Unknown"));

        verify(companyService, times(1)).getCompanyByName("Unknown");
    }

    @Test
    void createCompany_ShouldReturnCreatedCompany() throws Exception {
        when(companyService.saveCompany(any(CompanyDto.class))).thenReturn(companyDto);

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestCorp"))
                .andExpect(jsonPath("$.budget").value(100000.0))
                .andExpect(jsonPath("$.employeeIds.size()").value(2));

        verify(companyService, times(1)).saveCompany(any(CompanyDto.class));
    }

    @Test
    void createCompany_ShouldReturnInternalServerError_OnException() throws Exception {
        when(companyService.saveCompany(any(CompanyDto.class))).thenThrow(new InternalServerErrorException("Database error"));

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(companyService, times(1)).saveCompany(any(CompanyDto.class));
    }

    @Test
    void getCompanyWithEmployees_ShouldReturnCompanyWithEmployees() throws Exception {
        CompanyDto companyWithEmployees = CompanyDto.builder()
                .id(1L)
                .name("TestCorp")
                .budget(100000.0)
                .employeeIds(List.of(1L, 2L))
                .employees(List.of(
                        new UserDto(1L,"John", "Doe", "1234567890"),
                        new UserDto(2L,"Jane", "Smith", "0987654321")
                ))
                .build();

        when(companyService.getCompanyWithEmployees(1L)).thenReturn(companyWithEmployees);

        mockMvc.perform(get("/companies/1/with-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestCorp"))
                .andExpect(jsonPath("$.budget").value(100000.0))
                .andExpect(jsonPath("$.employeeIds.size()").value(2))
                .andExpect(jsonPath("$.employees.size()").value(2))
                .andExpect(jsonPath("$.employees[0].firstName").value("John"))
                .andExpect(jsonPath("$.employees[1].firstName").value("Jane"));

        verify(companyService, times(1)).getCompanyWithEmployees(1L);
    }
}
