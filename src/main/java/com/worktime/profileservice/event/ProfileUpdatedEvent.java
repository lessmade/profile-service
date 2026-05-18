package com.worktime.profileservice.event;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

import com.worktime.profileservice.model.enums.EmploymentType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileUpdatedEvent {

    private UUID employeeId;

    private String name;

    private String surname;

    private String phoneNumber;

    private String specialization;

    private EmploymentType employmentType;

    private String timezone;

    private LocalTime workStart;

    private LocalTime workEnd;

    private Instant updatedAt;

}