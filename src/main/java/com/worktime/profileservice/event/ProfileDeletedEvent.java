package com.worktime.profileservice.event;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDeletedEvent {
    
    private UUID employeeId;

    private String name;

    private String surname;

    private Instant deletedAt;
    
}
