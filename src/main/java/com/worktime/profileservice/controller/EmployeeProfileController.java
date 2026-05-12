package com.worktime.profileservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worktime.profileservice.model.request.EmployeeProfileRequest;
import com.worktime.profileservice.model.request.UpdateEmployeeProfileRequest;
import com.worktime.profileservice.model.response.EmployeeProfileResponse;
import com.worktime.profileservice.service.EmployeeProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    //TODO (@RolesAllowed)
    @GetMapping
    public List <EmployeeProfileResponse> getAllEmployess(){
        return employeeProfileService.getAllEmployees();
    }

    //TODO (@RolesAllowed)
    @GetMapping("/{employeeId}")
    public EmployeeProfileResponse getEmployeeById(@PathVariable UUID employeeId){
        return employeeProfileService.getEmployeeById(employeeId);
    }
    //TODO (@RolesAllowed)
    @PostMapping
    public EmployeeProfileResponse createEmployee(@Valid @RequestBody EmployeeProfileRequest request){
        return employeeProfileService.createEmployee(request);
    }
    //TODO (@RolesAllowed)
    @PatchMapping("/{employeeId}")
    public EmployeeProfileResponse updateEmployee(@PathVariable UUID employeeId, @RequestBody UpdateEmployeeProfileRequest request){
        return employeeProfileService.updateEmployee(employeeId, request);
    }
    //TODO (@RolesAllowed)
    @DeleteMapping("/{employeeId}")
    public void deleteEmployee(@PathVariable UUID employeeId){
        employeeProfileService.deleteEmployee(employeeId);
    }

    
}
