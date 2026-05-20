package com.worktime.profileservice.model.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.worktime.profileservice.model.enums.WorkDayExceptionType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkDayExceptionRequest {

    @NotNull
    private WorkDayExceptionType type;

    @NotNull
    @FutureOrPresent
    private LocalDate date;

    private LocalTime customStart;

    private LocalTime customEnd;

    @NotNull
    private WorkDayExceptionType type;

    private String reason;
}