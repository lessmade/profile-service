package com.worktime.profileservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.worktime.profileservice.model.request.EmployeeProfileRequest;
import com.worktime.profileservice.model.request.UpdateEmployeeProfileRequest;
import com.worktime.profileservice.model.response.EmployeeProfileResponse;
import com.worktime.profileservice.service.EmployeeProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    @GetMapping
    public EmployeeProfileResponse getEmployee(@RequestHeader("X-User-Id") Long authId) {
        return employeeProfileService.getEmployee(authId);
    }

    @GetMapping("/{employeeId}")
    public EmployeeProfileResponse getEmployee(@PathVariable UUID employeeId){
        return employeeProfileService.getEmployee(employeeId);
    }

    @GetMapping("/all")
    public List<EmployeeProfileResponse> getAllEmployees(){
        return employeeProfileService.getAllEmployees();
    }

    @GetMapping("/batch")
    public List<EmployeeProfileResponse> getByAuthIds(@RequestParam List<Long> authIds) {
        return employeeProfileService.getByAuthIds(authIds);
    }

    @PostMapping
    public EmployeeProfileResponse createEmployee(@RequestHeader("X-User-Id") Long authId,
                                                  @Valid @RequestBody EmployeeProfileRequest request){
        return employeeProfileService.createEmployee(authId, request);
    }
  
    @PatchMapping("/{employeeId}")
    public EmployeeProfileResponse updateEmployee(@PathVariable UUID employeeId,
                                                  @RequestBody UpdateEmployeeProfileRequest request){
        return employeeProfileService.updateEmployee(employeeId, request);
    }

    @PatchMapping()
    public EmployeeProfileResponse updateEmployee(@RequestHeader("X-User-Id") Long authId,
                                                  @RequestBody UpdateEmployeeProfileRequest request) {
        return employeeProfileService.updateEmployee(authId, request);
    }

    @DeleteMapping("/{employeeId}")
    public void deleteEmployee(@PathVariable UUID employeeId){
        employeeProfileService.deleteEmployee(employeeId);
    }
}
