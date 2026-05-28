package com.worktime.profileservice.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.event.ProfileCreatedEvent;
import com.worktime.profileservice.event.ProfileDeletedEvent;
import com.worktime.profileservice.event.ProfileUpdatedEvent;
import com.worktime.profileservice.exception.ProfileNotFoundException;
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
    
    private static final String PROFILE_CREATED_TOPIC = "profile.created";
    private static final String PROFILE_UPDATED_TOPIC = "profile.updated";
    private static final String PROFILE_DELETED_TOPIC = "profile.deleted";

    private EmployeeProfile getProfileByIdOrThrow(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));
    }
    @Transactional(readOnly = true)
    public List<EmployeeProfileResponse> getByAuthIds(List<Long> userIds) {
        return repository.findAllById(userIds)
                .stream()
                .map(mapper::toEmployeeProfileView)
                .toList();
    }

    @Transactional(readOnly = true)
    public List <EmployeeProfileResponse> getAllEmployees(){
        return repository.findAll()
                .stream()
                .map(mapper::toEmployeeProfileView)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getProfile(Long userId) {
        EmployeeProfile profile = getProfileByIdOrThrow(userId);
        return mapper.toEmployeeProfileView(profile);
    }

    private EmployeeProfileResponse updateProfile(EmployeeProfile profile, UpdateEmployeeProfileRequest request){
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
                                .userId(updatedProfile.getUserId())
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
    kafkaProducerService.sendEvent(PROFILE_UPDATED_TOPIC, event.getUserId().toString(), event);

    log.info("Обновленны данные сотрудника с id {}", updatedProfile.getUserId());

    return mapper.toEmployeeProfileView(updatedProfile);
    }

    @Transactional
    public EmployeeProfileResponse updateProfile(Long userId, UpdateEmployeeProfileRequest request) {
        EmployeeProfile profile = getProfileByIdOrThrow(userId);
        return updateProfile(profile, request);
    }

    @Transactional
    public EmployeeProfileResponse createProfile(Long userId, EmployeeProfileRequest request){
        EmployeeProfile profile = mapper.toEntity(request);
        profile.setUserId(userId);

        EmployeeProfile savedProfile = repository.save(profile);

        ProfileCreatedEvent event = ProfileCreatedEvent.builder()
                                    .userId(savedProfile.getUserId())
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
        kafkaProducerService.sendEvent(PROFILE_CREATED_TOPIC, event.getUserId().toString(), event);

        log.info("Создан сотрудник с id {}", savedProfile.getUserId());
        
        return mapper.toEmployeeProfileView(savedProfile);
    }

    @Transactional
    public void deleteProfile(Long userId){
        EmployeeProfile profile = getProfileByIdOrThrow(userId);

        repository.delete(profile);

        ProfileDeletedEvent event = ProfileDeletedEvent.builder()
                                    .userId(profile.getUserId())
                                    .name(profile.getName())
                                    .surname(profile.getSurname())
                                    .deletedAt(Instant.now())
                                    .build();
        kafkaProducerService.sendEvent(PROFILE_DELETED_TOPIC, event.getUserId().toString(), event);
        log.info("Удален сотрудник с id {}", profile.getUserId());
    }
}
