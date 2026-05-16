package com.worktime.profileservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.worktime.profileservice.event.ProfileCreatedEvent;
import com.worktime.profileservice.event.ProfileDeletedEvent;
import com.worktime.profileservice.event.ProfileUpdatedEvent;
import com.worktime.profileservice.event.WorkDayExceptionCreatedEvent;
import com.worktime.profileservice.event.WorkDayExceptionDeletedEvent;
import com.worktime.profileservice.event.WorkDayExceptionStatusChangedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendProfileUpdatedEvent(ProfileUpdatedEvent event){

        kafkaTemplate.send(
            "profile.events",
            event.getEmployeeId().toString(),
            event
        );
        log.info("ProfileUpdatedEvent sent for employeeId{}",event.getEmployeeId());
    }

    public void sendProfileCreatedEvent(ProfileCreatedEvent event){

        kafkaTemplate.send(
            "profile.events",
            event.getEmployeeId().toString(),
            event
        );
        log.info("ProfileCreatedEvent sent for employeeId{}",event.getEmployeeId());
    
    }

    public void sendProfileDeletedEvent(ProfileDeletedEvent event){

        kafkaTemplate.send(
            "profile.events",
            event.getEmployeeId().toString(),
            event
        );
        log.info("ProfileDeletedEvent sent for employeeId{}",event.getEmployeeId());
    
    }
    public void sendExceptionCreatedEvent(WorkDayExceptionCreatedEvent event){

        kafkaTemplate.send(
            "workday-exceptions.events",
            event.getEmployeeId().toString(),
            event
        );
        log.info("WorkDayExceptionsCreatedEvent sent for employeeId{}",event.getEmployeeId());

    }
    public void sendExceptionsDeletedEvent(WorkDayExceptionDeletedEvent event){

        kafkaTemplate.send(
            "workday-exceptions.events",
            event.getEmployeeId().toString(),
            event
        );
        log.info("WorkDayExceptionsDeletedEvent sent for employeeId{}",event.getEmployeeId());

    }


    public void sendExceptionsStatusChangedEvent(WorkDayExceptionStatusChangedEvent event){

        kafkaTemplate.send(
            "workday-exceptions.events",
            event.getEmployeeId().toString(),
            event
        );
        log.info("WorkDayExceptionsStatusChangedEvent sent for employeeId{}",event.getEmployeeId());

    }
    

}
