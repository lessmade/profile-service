package com.worktime.profileservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worktime.profileservice.model.request.WorkDayExceptionRequest;
import com.worktime.profileservice.model.response.WorkDayExceptionResponse;
import com.worktime.profileservice.service.WorkDayExceptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/work-day-exceptions")
@RequiredArgsConstructor
public class WorkDayExceptionController {

    private final WorkDayExceptionService workDayExceptionService;

    @GetMapping
    public List<WorkDayExceptionResponse> getAllExceptions(){
        return workDayExceptionService.getAllExceptions();
    }

    @GetMapping("/{exceptionId}")
    public WorkDayExceptionResponse getExceptionById(@PathVariable UUID exceptionId){
        return workDayExceptionService.getExceptionById(exceptionId);
    }

    @GetMapping("/profile/{employeeId}")
    public List<WorkDayExceptionResponse> getEmployeeExceptions(@PathVariable UUID employeeId){
        return workDayExceptionService.getEmployeeExceptions(employeeId);
    }

    @PostMapping
    public WorkDayExceptionResponse createException(@Valid @RequestBody WorkDayExceptionRequest request){
        return workDayExceptionService.createException(request);
    }

    @DeleteMapping("/{exceptionId}")
    public void deleteException(@PathVariable UUID exceptionId){
        workDayExceptionService.deleteException(exceptionId);
    }
}
