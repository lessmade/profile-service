package com.worktime.profileservice.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.event.ProfileCreatedEvent;
import com.worktime.profileservice.event.ProfileDeletedEvent;
import com.worktime.profileservice.event.ProfileUpdatedEvent;
import com.worktime.profileservice.mapper.EmployeeProfileMapper;
import com.worktime.profileservice.model.request.EmployeeProfileRequest;
import com.worktime.profileservice.model.request.UpdateEmployeeProfileRequest;
import com.worktime.profileservice.model.response.EmployeeProfileResponse;
import com.worktime.profileservice.repository.EmployeeProfileRepository;

import com.worktime.profileservice.kafka.KafkaProducerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeProfileService {

    private final EmployeeProfileRepository repository;
    private final KafkaProducerService kafkaProducerService;
    private final EmployeeProfileMapper mapper;
    private static final String PROFILE_TOPIC = "profile.events";

    private EmployeeProfile getEmployeeOrThrow(UUID employeeId){
        return repository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));
    }
    private EmployeeProfile getEmployeeByAuthIdOrThrow(Long authId) {
        return repository.findByAuthId(authId)
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));
    }
    @Transactional(readOnly = true)
    public List <EmployeeProfileResponse> getAllEmployees(){
        return repository.findAll()
                .stream()
                .map(mapper::toEmployeeProfileView)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getEmployee(Long authId) {
        EmployeeProfile profile = getEmployeeByAuthIdOrThrow(authId);
        return mapper.toEmployeeProfileView(profile);
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getEmployee(UUID employeeId){
        EmployeeProfile profile = getEmployeeOrThrow(employeeId);
        return mapper.toEmployeeProfileView(profile);
    }

    private EmployeeProfileResponse updateEmployee(EmployeeProfile profile, UpdateEmployeeProfileRequest request){
    if (request.getName() != null) {
        profile.setName(request.getName());
    }

    if (request.getSurname() != null) {
        profile.setSurname(request.getSurname());
    }

    if (request.getPhoneNumber() != null) {
        profile.setPhoneNumber(request.getPhoneNumber());
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

    ProfileUpdatedEvent event = ProfileUpdatedEvent.builder()
                                .employeeId(updatedProfile.getId())
                                .name(updatedProfile.getName())
                                .surname(updatedProfile.getSurname())
                                .phoneNumber(updatedProfile.getPhoneNumber())
                                .specialization(updatedProfile.getSpecialization())
                                .employmentType(updatedProfile.getEmploymentType())
                                .timezone(updatedProfile.getTimezone())
                                .workStart(updatedProfile.getWorkStart())
                                .workEnd(updatedProfile.getWorkEnd())
                                .updatedAt(Instant.now())
                                .build();
    //kafkaProducerService.sendEvent(PROFILE_TOPIC, event.getEmployeeId().toString(), event);

    log.info("Обновленны данные сотрудника с id {}", updatedProfile.getId());

    return mapper.toEmployeeProfileView(updatedProfile);
    }

    @Transactional
    public EmployeeProfileResponse updateEmployee(UUID employeeId, UpdateEmployeeProfileRequest request) {
        EmployeeProfile profile = getEmployeeOrThrow(employeeId);
        return updateEmployee(profile, request);
    }

    @Transactional
    public EmployeeProfileResponse updateEmployee(Long authId, UpdateEmployeeProfileRequest request) {
        EmployeeProfile profile = getEmployeeByAuthIdOrThrow(authId);
        return updateEmployee(profile, request);
    }

    @Transactional
    public EmployeeProfileResponse createEmployee(Long authId, EmployeeProfileRequest request){
        EmployeeProfile profile = mapper.toEntity(request);
        profile.setAuthId(authId);

        EmployeeProfile savedProfile = repository.save(profile);

        ProfileCreatedEvent event = ProfileCreatedEvent.builder()
                                    .employeeId(savedProfile.getId())
                                    .name(savedProfile.getName())
                                    .surname(savedProfile.getSurname())
                                    .phoneNumber(savedProfile.getPhoneNumber())
                                    .specialization(savedProfile.getSpecialization())
                                    .employmentType(savedProfile.getEmploymentType())
                                    .timezone(savedProfile.getTimezone())
                                    .workStart(savedProfile.getWorkStart())
                                    .workEnd(savedProfile.getWorkEnd())
                                    .createdAt(Instant.now())
                                    .build();
        //kafkaProducerService.sendEvent(PROFILE_TOPIC, event.getEmployeeId().toString(), event);

        log.info("Создан сотрудник с id {}", savedProfile.getId());
        
        return mapper.toEmployeeProfileView(savedProfile);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId){
        EmployeeProfile profile = getEmployeeOrThrow(employeeId);

        repository.delete(profile);

        ProfileDeletedEvent event = ProfileDeletedEvent.builder()
                                    .employeeId(profile.getId())
                                    .name(profile.getName())
                                    .surname(profile.getSurname())
                                    .deletedAt(Instant.now())
                                    .build();
        //kafkaProducerService.sendEvent(PROFILE_TOPIC, event.getEmployeeId().toString(), event);
        log.info("Удален сотрудник с id {}", profile.getId());
    }
}

