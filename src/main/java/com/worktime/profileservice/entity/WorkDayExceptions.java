package com.worktime.profileservice.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.worktime.profileservice.model.enums.WorkDayExceptionStatus;
import com.worktime.profileservice.model.enums.WorkDayExceptionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "work_day_exceptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkDayExceptions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeProfile employee;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime customStart;

    private LocalTime customEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkDayExceptionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkDayExceptionStatus status;

    private String reason;

    //TODO rename to 
}