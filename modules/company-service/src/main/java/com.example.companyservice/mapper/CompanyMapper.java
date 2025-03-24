package com.example.companyservice.mapper;

import com.example.commondto.CompanyDto;
import com.example.companyservice.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDto toDto(Company company);
    Company toEntity(CompanyDto companyDto);
    List<CompanyDto> toDtoList(List<Company> companies);
}
