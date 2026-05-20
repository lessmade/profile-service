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
    public EmployeeProfileResponse getProfile(@RequestHeader("X-User-Id") Long userId) {
        return employeeProfileService.getProfile(userId);
    }

    @GetMapping("/{userId}")
    public EmployeeProfileResponse getEmployeeProfile(@PathVariable Long userId){
        return employeeProfileService.getProfile(userId);
    }

    @GetMapping("/all")
    public List <EmployeeProfileResponse> getAllEmployeeProfiles(){
        return employeeProfileService.getAllEmployees();
    }

    @GetMapping("/batch")
    public List<EmployeeProfileResponse> getByAuthIds(@RequestParam List<Long> authIds) {
        return employeeProfileService.getByAuthIds(authIds);
    }

    @PostMapping
    public EmployeeProfileResponse createProfile(@RequestHeader("X-User-Id") Long userId,
                                                  @Valid @RequestBody EmployeeProfileRequest request){
        return employeeProfileService.createProfile(userId, request);
    }
  
    @PatchMapping("/{userId}")
    public EmployeeProfileResponse updateEmployeeProfile(@PathVariable Long userId,
                                                  @RequestBody UpdateEmployeeProfileRequest request){
        return employeeProfileService.updateProfile(userId, request);
    }

    @PatchMapping
    public EmployeeProfileResponse updateProfile(@RequestHeader("X-User-Id") Long userId,
                                                  @RequestBody UpdateEmployeeProfileRequest request) {
        return employeeProfileService.updateProfile(userId, request);
    }

    @DeleteMapping("/{userId}")
    public void deleteEmployeeProfile(@PathVariable Long userId){
        employeeProfileService.deleteProfile(userId);
    }
}
