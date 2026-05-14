package com.worktime.profileservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worktime.profileservice.model.request.VacationRequest;
import com.worktime.profileservice.model.response.VacationResponse;
import com.worktime.profileservice.service.VacationService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/vacations")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;

    @GetMapping
    public List<VacationResponse> getAllVacations(){
        return vacationService.getAllVacations();
    }

    @GetMapping("/{vacationId}")
    public VacationResponse getVacationById(@PathVariable UUID vacationId){
        return vacationService.getVacationById(vacationId);
    }

    @GetMapping("/profile/{employeeId}")
    public List<VacationResponse> getEmployeeVacations(@PathVariable UUID employeeId){
        return vacationService.getEmployeeVacations(employeeId);
    }

    @PostMapping
    public VacationResponse createVacation(@Valid @RequestBody VacationRequest request){
        return vacationService.createVacation(request);
    }

    @PatchMapping("/{vacationId}/approve")
    public VacationResponse approveVacation(@PathVariable UUID vacationId){
        return vacationService.approveVacation(vacationId);
    }

    @PatchMapping("/{vacationId}/cancel")
    public VacationResponse cancelVacation(@PathVariable UUID vacationId){
        return vacationService.cancelVacation(vacationId);
    }

    @PatchMapping("/{vacationId}/reject")
    public VacationResponse rejectVacation(@PathVariable UUID vacationId){
        return vacationService.rejectVacation(vacationId);
    }
    

}
