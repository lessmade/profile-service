package com.worktime.profileservice.model.response;

import java.time.LocalDate;
import java.util.UUID;

import com.worktime.profileservice.model.enums.WorkDayExceptionStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VacationResponse {

    private UUID id;

    private UUID employeeId;

    private String employeeName;

    private String employeeSurname;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private WorkDayExceptionStatus status;
}