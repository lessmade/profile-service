package com.worktime.profileservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.worktime.profileservice.model.enums.WorkDayExceptionStatus;
import com.worktime.profileservice.model.request.UpdateExceptionStatusRequest;
import com.worktime.profileservice.model.request.WorkDayExceptionRequest;
import com.worktime.profileservice.model.response.WorkDayExceptionResponse;
import com.worktime.profileservice.service.WorkDayExceptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/profiles/work-day-exceptions")
@RequiredArgsConstructor
public class WorkDayExceptionController {

    private final WorkDayExceptionService workDayExceptionService;

    @GetMapping("/all")
    public List<WorkDayExceptionResponse> getAllExceptions(){
        return workDayExceptionService.getAllExceptions();
    }

    @GetMapping
    public List<WorkDayExceptionResponse> getExceptions(@RequestHeader("X-User-Id") Long userId){
        return workDayExceptionService.getExceptions(userId);
    }

    @GetMapping("/{userId}")
    public List<WorkDayExceptionResponse> getEmployeeExceptions(@PathVariable Long userId){
        return workDayExceptionService.getExceptions(userId);
    }

    @PostMapping
    public WorkDayExceptionResponse createException(@RequestHeader("X-User-Id") Long userId,
                                                    @Valid @RequestBody WorkDayExceptionRequest request) {
        return workDayExceptionService.createException(userId, request);
    }

    // TODO: check if user is admin or exception belongs to user
    @PatchMapping("/{exceptionId}/status")
    public WorkDayExceptionResponse updateExceptionStatus(@PathVariable UUID exceptionId,
                                                          @RequestBody UpdateExceptionStatusRequest request){
        return workDayExceptionService.updateStatus(exceptionId, request);
    }

    @DeleteMapping("/{exceptionId}")
    public void deleteException(@RequestHeader("X-User-Id") Long userId,
                                @PathVariable UUID exceptionId){
        workDayExceptionService.deleteException(exceptionId);
    }
}
