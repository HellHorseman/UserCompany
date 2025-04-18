package com.example.commondto.companyDtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponseDto {
    private Long id;
    private String name;
    private Double budget;
    private String description;
    private Integer totalEmployees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
