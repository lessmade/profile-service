package com.worktime.profileservice.mapper;

import org.springframework.stereotype.Component;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.entity.Vacation;
import com.worktime.profileservice.model.enums.VacationStatus;
import com.worktime.profileservice.model.request.VacationRequest;
import com.worktime.profileservice.model.response.VacationResponse;


@Component
public class VacationMapper {

    public Vacation toEntity(VacationRequest request, EmployeeProfile employee){

        Vacation vacation = new Vacation();

        vacation.setEmployee(employee);
        vacation.setStartDate(request.getStartDate());
        vacation.setEndDate(request.getEndDate());
        vacation.setReason(request.getReason());
        vacation.setStatus(VacationStatus.PLANNED);

        return vacation;
    }

    
    public VacationResponse toVacationView(Vacation vacation){
        return VacationResponse.builder()
                .id(vacation.getId())
                .employeeId(vacation.getEmployee().getId())
                .employeeName(vacation.getEmployee().getName())
                .employeeSurname(vacation.getEmployee().getSurname())
                .startDate(vacation.getStartDate())
                .endDate(vacation.getEndDate())
                .reason(vacation.getReason())
                .status(vacation.getStatus())
                .build();

    }
    
}
