package com.example.commondto.companyDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequestDto {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @PositiveOrZero(message = "Budget must be positive or zero")
    private Double budget;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
