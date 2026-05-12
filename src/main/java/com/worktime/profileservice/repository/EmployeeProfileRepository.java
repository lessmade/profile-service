package com.worktime.profileservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktime.profileservice.entity.EmployeeProfile;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, UUID> {

    
}
