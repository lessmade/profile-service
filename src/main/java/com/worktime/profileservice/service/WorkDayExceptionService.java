package com.worktime.profileservice.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.entity.WorkDayExceptions;
import com.worktime.profileservice.event.WorkDayExceptionCreatedEvent;
import com.worktime.profileservice.event.WorkDayExceptionDeletedEvent;
import com.worktime.profileservice.event.WorkDayExceptionStatusChangedEvent;
import com.worktime.profileservice.kafka.KafkaProducerService;
import com.worktime.profileservice.mapper.WorkDayExceptionMapper;
import com.worktime.profileservice.model.enums.WorkDayExceptionStatus;
import com.worktime.profileservice.model.request.UpdateExceptionStatusRequest;
import com.worktime.profileservice.model.request.WorkDayExceptionRequest;
import com.worktime.profileservice.model.response.WorkDayExceptionResponse;
import com.worktime.profileservice.repository.EmployeeProfileRepository;
import com.worktime.profileservice.repository.WorkDayExceptionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkDayExceptionService {
    
    private final WorkDayExceptionRepository repository;
    private final KafkaProducerService kafkaProducerService;
    private final EmployeeProfileRepository employeeRepository;
    private final WorkDayExceptionMapper mapper;
    private static final String WORKDAY_EXCEPTION_TOPIC = "workday-exception.events";

    private WorkDayExceptions getExceptionOrThrow(UUID exceptionId) {
        return repository.findById(exceptionId)
            .orElseThrow(() ->
                new RuntimeException("Исключения рабочих дней не найдены"));
    }

    @Transactional(readOnly = true)
    public List<WorkDayExceptionResponse> getAllExceptions(){
        return repository.findAll()
                .stream()
                .map(mapper::toWorkDayExceptionView)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkDayExceptionResponse getExceptionById(UUID exceptionId){
        WorkDayExceptions exception = getExceptionOrThrow(exceptionId);

        return mapper.toWorkDayExceptionView(exception);
    }
    @Transactional(readOnly = true)
    public List<WorkDayExceptionResponse> getEmployeeExceptions(UUID employeeId){
        return repository.findByEmployee_Id(employeeId)
                .stream()
                .map(mapper::toWorkDayExceptionView)
                .toList();                                 
    }

    @Transactional
    public WorkDayExceptionResponse createException(WorkDayExceptionRequest request){
        EmployeeProfile employee = employeeRepository.findById(request.getEmployeeId())
                                    .orElseThrow(()-> new RuntimeException("Сотрудник не найден"));

        WorkDayExceptions workDayException = mapper.toEntity(request, employee);
        WorkDayExceptions savedException = repository.save(workDayException);

        WorkDayExceptionCreatedEvent event = WorkDayExceptionCreatedEvent.builder()
                                                .exceptionId(savedException.getId())
                                                    .employeeId(savedException.getEmployee().getId())
                                                    .date(savedException.getDate())
                                                    .customStart(savedException.getCustomStart())
                                                    .customEnd(savedException.getCustomEnd())
                                                    .type(savedException.getType())
                                                    .status(savedException.getStatus())
                                                    .reason(savedException.getReason())
                                                    .createdAt(Instant.now())
                                                    .build();
        kafkaProducerService.sendEvent(WORKDAY_EXCEPTION_TOPIC, event.getEmployeeId().toString(), event);

        log.info("Заявка на исключения в рабочих днях создана для сотрудника {}",employee.getId());

        return mapper.toWorkDayExceptionView(savedException);
    }

    @Transactional
    public WorkDayExceptionResponse updateStatus(UUID exceptionId, UpdateExceptionStatusRequest request){
        WorkDayExceptions exception = getExceptionOrThrow(exceptionId);

        exception.setStatus(request.getStatus());

        WorkDayExceptions updatedException = repository.save(exception);

        WorkDayExceptionStatusChangedEvent event = WorkDayExceptionStatusChangedEvent.builder()
                                                    .exceptionId(updatedException.getId())
                                                    .employeeId(updatedException.getEmployee().getId())
                                                    .date(updatedException.getDate())
                                                    .customStart(updatedException.getCustomStart())
                                                    .customEnd(updatedException.getCustomEnd())
                                                    .type(updatedException.getType())
                                                    .status(updatedException.getStatus())
                                                    .reason(updatedException.getReason())
                                                    .occurredAt(Instant.now())
                                                    .build();

        kafkaProducerService.sendEvent(WORKDAY_EXCEPTION_TOPIC, event.getEmployeeId().toString(), event);

        log.info("Исключение рабочих дней {} обновлено со статусом {}",updatedException.getId(),request.getStatus());

        return mapper.toWorkDayExceptionView(updatedException);
    }

    @Transactional
    public void deleteException(UUID exceptionId){
        WorkDayExceptions exception = getExceptionOrThrow(exceptionId);

        repository.delete(exception);

        WorkDayExceptionDeletedEvent event = WorkDayExceptionDeletedEvent.builder()
                                                .employeeId(exception.getEmployee().getId())
                                                .exceptionId(exception.getId())
                                                .date(exception.getDate())
                                                .deletedAt(Instant.now())
                                                .build();
        kafkaProducerService.sendEvent(WORKDAY_EXCEPTION_TOPIC, event.getEmployeeId().toString(), event);
        log.info("Иcключение в рабочих днях {} удалено",exception.getId());
    }
}

