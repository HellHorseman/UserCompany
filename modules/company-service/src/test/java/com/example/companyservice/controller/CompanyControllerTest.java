package com.example.companyservice.controller;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
@ExtendWith(MockitoExtension.class)
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
        companyDto = new CompanyDto("TestCorp", 100000.0, List.of(1L, 2L, 3L));
    }

    @Test
    void getAllCompanies_ShouldReturnList() throws Exception {
        when(companyService.getAllCompanies()).thenReturn(List.of(companyDto));

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("TestCorp"));

        verify(companyService, times(1)).getAllCompanies();
    }

    @Test
    void getAllCompanies_ShouldReturnNoContent_WhenEmpty() throws Exception {
        when(companyService.getAllCompanies()).thenReturn(List.of());

        mockMvc.perform(get("/companies"))
                .andExpect(status().isNoContent());

        verify(companyService, times(1)).getAllCompanies();
    }

    @Test
    void getCompanyById_ShouldReturnCompany_WhenExists() throws Exception {
        when(companyService.getCompanyById(1L)).thenReturn(Optional.of(companyDto));

        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCorp"));

        verify(companyService, times(1)).getCompanyById(1L);
    }

    @Test
    void getCompanyById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(companyService.getCompanyById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/companies/99"))
                .andExpect(status().isNotFound());

        verify(companyService, times(1)).getCompanyById(99L);
    }

    @Test
    void getCompanyByName_ShouldReturnCompany_WhenExists() throws Exception {
        when(companyService.getCompanyByName("TestCorp")).thenReturn(Optional.of(companyDto));

        mockMvc.perform(get("/companies/name/TestCorp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCorp"));

        verify(companyService, times(1)).getCompanyByName("TestCorp");
    }

    @Test
    void getCompanyByName_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(companyService.getCompanyByName("Unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/companies/name/Unknown"))
                .andExpect(status().isNotFound());

        verify(companyService, times(1)).getCompanyByName("Unknown");
    }

    @Test
    void createCompany_ShouldReturnCreatedCompany() throws Exception {
        when(companyService.saveCompany(any(CompanyDto.class))).thenReturn(companyDto);

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCorp"));

        verify(companyService, times(1)).saveCompany(any(CompanyDto.class));
    }

    @Test
    void createCompany_ShouldReturnInternalServerError_OnException() throws Exception {
        when(companyService.saveCompany(any(CompanyDto.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyDto)))
                .andExpect(status().isInternalServerError());

        verify(companyService, times(1)).saveCompany(any(CompanyDto.class));
    }
}
