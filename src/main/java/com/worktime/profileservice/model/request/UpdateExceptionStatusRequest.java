package com.worktime.profileservice.model.request;

import com.worktime.profileservice.model.enums.WorkDayExceptionStatus;

import lombok.Data;

@Data
public class UpdateExceptionStatusRequest {

    private WorkDayExceptionStatus status;
    
}
