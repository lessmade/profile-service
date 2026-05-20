package com.worktime.profileservice.event;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.worktime.profileservice.model.enums.WorkDayExceptionStatus;
import com.worktime.profileservice.model.enums.WorkDayExceptionType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkDayExceptionStatusChangedEvent {

    private UUID exceptionId;

    private Long userId;

    private LocalDate date;

    private LocalTime customStart;

    private LocalTime customEnd;

    private WorkDayExceptionType type;

    private WorkDayExceptionStatus status;

    private String reason;

    private Instant occurredAt;
}