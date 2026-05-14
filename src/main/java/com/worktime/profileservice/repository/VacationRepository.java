package com.worktime.profileservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktime.profileservice.entity.Vacation;

public interface VacationRepository extends JpaRepository<Vacation, UUID> {
    List<Vacation> findByEmployee_Id(UUID employeeId);
    
}
