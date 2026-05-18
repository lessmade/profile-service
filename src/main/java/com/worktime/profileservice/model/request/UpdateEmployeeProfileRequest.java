package com.worktime.profileservice.model.request;

import java.time.LocalTime;

import com.worktime.profileservice.model.enums.EmploymentType;

import lombok.Data;

@Data
public class UpdateEmployeeProfileRequest {
    private String name;

    private String surname;

    private String phoneNumber;

    private String specialization;

    private EmploymentType employmentType;

    private String timezone;

    private LocalTime workStart;

    private LocalTime workEnd;

    
}
