package com.worktime.profileservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktime.profileservice.entity.WorkDayExceptions;

public interface WorkDayExceptionRepository extends JpaRepository<WorkDayExceptions, UUID> {
    List<WorkDayExceptions> findByEmployee_Id(UUID employeeId);
}
