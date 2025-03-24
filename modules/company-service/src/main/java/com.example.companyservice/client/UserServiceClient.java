package com.example.companyservice.client;

import com.example.commondto.CompanyDto;
import com.example.commondto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service", url = "http://user-service:8081")
public interface UserServiceClient {

    @GetMapping("/companies/{id}")
    CompanyDto getCompanyById(@PathVariable Long id);

    @GetMapping ("/users/by-ids")
    List<UserDto> getUsersByIds(@RequestBody List<Long> ids);
}
