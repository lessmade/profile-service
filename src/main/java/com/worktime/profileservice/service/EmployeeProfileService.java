package com.worktime.profileservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.mapper.EmployeeProfileMapper;
import com.worktime.profileservice.model.request.EmployeeProfileRequest;
import com.worktime.profileservice.model.request.UpdateEmployeeProfileRequest;
import com.worktime.profileservice.model.response.EmployeeProfileResponse;
import com.worktime.profileservice.repository.EmployeeProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeProfileService {

    private EmployeeProfileRepository repository;

    private EmployeeProfileMapper mapper;

    @Transactional(readOnly = true)
    public List <EmployeeProfileResponse> getAllEmployees(){
        return repository.findAll()
                .stream()
                .map(mapper::toEmployeeProfileView)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getEmployeeById(UUID employeeId){
        EmployeeProfile profile = repository.findById(employeeId)
                                        .orElseThrow(()-> new RuntimeException("Сотрудник не найден"));

        return mapper.toEmployeeProfileView(profile);
    }

    @Transactional
    public EmployeeProfileResponse updateEmployee(UUID employeeId, UpdateEmployeeProfileRequest request){
        EmployeeProfile profile = repository.findById(employeeId)
                                        .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));
    if (request.getName() != null) {
        profile.setName(request.getName());
    }

    if (request.getSurname() != null) {
        profile.setSurname(request.getSurname());
    }

    if (request.getSpecialization() != null) {
        profile.setSpecialization(request.getSpecialization());
    }

    if (request.getEmploymentType() != null) {
        profile.setEmploymentType(request.getEmploymentType());
    }

    if (request.getTimezone() != null) {
        profile.setTimezone(request.getTimezone());
    }

    if (request.getWorkStart() != null) {
        profile.setWorkStart(request.getWorkStart());
    }

    if (request.getWorkEnd() != null) {
        profile.setWorkEnd(request.getWorkEnd());
    }
    EmployeeProfile updatedProfile = repository.save(profile);

    log.info("Обновленны данные сотрудника с id {}", updatedProfile.getId());

    return mapper.toEmployeeProfileView(updatedProfile);
    }

    @Transactional
    public EmployeeProfileResponse createEmployee(EmployeeProfileRequest request){
        EmployeeProfile profile = mapper.toEntity(request);

        EmployeeProfile savedProfile = repository.save(profile);

        log.info("Создан сотрудник с id {}", savedProfile.getId());
        
        return mapper.toEmployeeProfileView(savedProfile);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId){
        if (!repository.existsById(employeeId)) {
            throw new RuntimeException("Сотрудник не найден");
        }
        repository.deleteById(employeeId);
    }
}


// TODO cusom exeptions (ОБЯЗАТЕЛЬНО)

