package com.worktime.profileservice.mapper;

import org.springframework.stereotype.Component;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.entity.WorkDayExceptions;
import com.worktime.profileservice.model.enums.WorkDayExceptionStatus;
import com.worktime.profileservice.model.request.WorkDayExceptionRequest;
import com.worktime.profileservice.model.response.WorkDayExceptionResponse;

@Component
public class WorkDayExceptionMapper {

    public WorkDayExceptions toEntity(WorkDayExceptionRequest request, EmployeeProfile employee){

        WorkDayExceptions workDayExceptions = new WorkDayExceptions();

        workDayExceptions.setEmployee(employee);
        workDayExceptions.setStartAt(request.getDatetimeStart());
        workDayExceptions.setEndAt(request.getDatetimeEnd());
        workDayExceptions.setType(request.getType());
        workDayExceptions.setStatus(WorkDayExceptionStatus.PLANNED);
        
        workDayExceptions.setReason(request.getReason());

        return workDayExceptions;
    }

    public WorkDayExceptionResponse toWorkDayExceptionView(WorkDayExceptions workDayExceptions){
        return WorkDayExceptionResponse.builder()
                .id(workDayExceptions.getId())
                .userId(workDayExceptions.getEmployee().getUserId())
                .datetimeStart(workDayExceptions.getStartAt())
                .datetimeEnd(workDayExceptions.getEndAt())
                .type(workDayExceptions.getType())
                .status(workDayExceptions.getStatus())
                .reason(workDayExceptions.getReason())
                .build();
    }
}
