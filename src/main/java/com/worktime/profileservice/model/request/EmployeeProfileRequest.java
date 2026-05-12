package com.worktime.profileservice.model.request;

import java.time.LocalTime;

import com.worktime.profileservice.model.enums.EmploymentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeProfileRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String specialization;

    @NotNull
    private EmploymentType employmentType;

    @NotBlank
    private String timezone;

    @NotNull
    private LocalTime workStart;

    @NotNull
    private LocalTime workEnd;



    
}
