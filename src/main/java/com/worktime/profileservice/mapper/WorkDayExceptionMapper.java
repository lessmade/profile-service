package com.worktime.profileservice.mapper;

import org.springframework.stereotype.Component;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.entity.WorkDayExceptions;
import com.worktime.profileservice.model.request.WorkDayExceptionRequest;
import com.worktime.profileservice.model.response.WorkDayExceptionResponse;

@Component
public class WorkDayExceptionMapper {

    public WorkDayExceptions toEntity(WorkDayExceptionRequest request, EmployeeProfile employee){

        WorkDayExceptions workDayExceptions = new WorkDayExceptions();

        workDayExceptions.setEmployee(employee);
        workDayExceptions.setDate(request.getDate());
        workDayExceptions.setCustomStart(request.getCustomStart());
        workDayExceptions.setCustomEnd(request.getCustomEnd());
        workDayExceptions.setType(request.getType());
        workDayExceptions.setReason(request.getReason());

        return workDayExceptions;
    }

    public WorkDayExceptionResponse toWorkDayExceptionView(WorkDayExceptions workDayExceptions){
        return WorkDayExceptionResponse.builder()
                .id(workDayExceptions.getId())
                .employeeId(workDayExceptions.getEmployee().getId())
                .employeeName(workDayExceptions.getEmployee().getName())
                .employeeSurname(workDayExceptions.getEmployee().getSurname())
                .date(workDayExceptions.getDate())
                .customStart(workDayExceptions.getCustomStart())
                .customEnd(workDayExceptions.getCustomEnd())
                .type(workDayExceptions.getType())
                .reason(workDayExceptions.getReason())
                .build();
    }
}
