package com.worktime.profileservice.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.worktime.profileservice.model.enums.WorkDayExceptionStatus;
import com.worktime.profileservice.model.enums.WorkDayExceptionType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkDayExceptionResponse {

    private UUID id;

    private Long userId;

    private LocalDateTime datetimeStart;

    private LocalDateTime datetimeEnd;

    private WorkDayExceptionType type;

    private WorkDayExceptionStatus status;

    private String reason;
}