package com.worktime.profileservice.event;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkDayExceptionDeletedEvent {

    private UUID exceptionId;

    private UUID employeeId;

    private LocalDate date;

    private Instant deletedAt;
}
