package com.worktime.profileservice.event;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkDayExceptionDeletedEvent {

    private UUID exceptionId;

    private Long userId;

    private LocalDateTime start;

    private LocalDateTime end;

    private Instant deletedAt;
}
