package com.worktime.profileservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktime.profileservice.entity.EmployeeProfile;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, UUID> {
    Optional<EmployeeProfile> findByAuthId(Long authId);
}
