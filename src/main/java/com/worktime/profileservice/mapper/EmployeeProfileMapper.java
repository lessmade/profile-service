package com.worktime.profileservice.mapper;

import org.springframework.stereotype.Component;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.model.request.EmployeeProfileRequest;
import com.worktime.profileservice.model.response.EmployeeProfileResponse;

@Component
public class EmployeeProfileMapper {
    public EmployeeProfile toEntity(EmployeeProfileRequest request){

        EmployeeProfile profile = new EmployeeProfile();

        profile.setName(request.getName());
        profile.setSurname(request.getSurname());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setSpecialization(request.getSpecialization());
        profile.setEmploymentType(request.getEmploymentType());
        profile.setTimezone(request.getTimezone());
        profile.setWorkStart(request.getWorkStart());
        profile.setWorkEnd(request.getWorkEnd());

        return profile;
    }

    public EmployeeProfileResponse toEmployeeProfileView(EmployeeProfile profile){
        return EmployeeProfileResponse.builder()
                .id(profile.getId())
                .authId(profile.getAuthId())
                .name(profile.getName())
                .surname(profile.getSurname())
                .phoneNumber(profile.getPhoneNumber())
                .specialization(profile.getSpecialization())
                .employmentType(profile.getEmploymentType())
                .timezone(profile.getTimezone())
                .workStart(profile.getWorkStart())
                .workEnd(profile.getWorkEnd())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
    
}
