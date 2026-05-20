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
    public List<WorkDayExceptionResponse> getExceptions(Long userId){
        return repository.findByEmployee_userId(userId)
                .stream()
                .map(mapper::toWorkDayExceptionView)
                .toList();                                 
    }

    @Transactional
    public WorkDayExceptionResponse createException(Long userId, WorkDayExceptionRequest request){
        EmployeeProfile employee = employeeRepository.findById(userId)
                                    .orElseThrow(()-> new RuntimeException("Сотрудник не найден"));

        WorkDayExceptions workDayException = mapper.toEntity(request, employee);
        WorkDayExceptions savedException = repository.save(workDayException);

        WorkDayExceptionCreatedEvent event = WorkDayExceptionCreatedEvent.builder()
                                                    .exceptionId(savedException.getId())
                                                    .userId(savedException.getEmployee().getUserId())
                                                    .start(savedException.getStartAt())
                                                    .end(savedException.getEndAt())
                                                    .type(savedException.getType())
                                                    .status(savedException.getStatus())
                                                    .reason(savedException.getReason())
                                                    .createdAt(Instant.now())
                                                    .build();
        kafkaProducerService.sendEvent(WORKDAY_EXCEPTION_TOPIC, event.getUserId().toString(), event);

        log.info("Заявка на исключения в рабочих днях создана для сотрудника {}",employee.getUserId());

        return mapper.toWorkDayExceptionView(savedException);
    }

    @Transactional
    public WorkDayExceptionResponse updateStatus(UUID exceptionId, UpdateExceptionStatusRequest request){
        WorkDayExceptions exception = getExceptionOrThrow(exceptionId);

        exception.setStatus(request.getStatus());

        WorkDayExceptions updatedException = repository.save(exception);

        WorkDayExceptionStatusChangedEvent event = WorkDayExceptionStatusChangedEvent.builder()
                                                    .exceptionId(updatedException.getId())
                                                    .userId(updatedException.getEmployee().getUserId())
                                                    .start(updatedException.getStartAt())
                                                    .end(updatedException.getEndAt())
                                                    .type(updatedException.getType())
                                                    .status(updatedException.getStatus())
                                                    .reason(updatedException.getReason())
                                                    .occurredAt(Instant.now())
                                                    .build();

        kafkaProducerService.sendEvent(WORKDAY_EXCEPTION_TOPIC, event.getUserId().toString(), event);

        log.info("Исключение рабочих дней {} обновлено со статусом {}",updatedException.getId(),request.getStatus());

        return mapper.toWorkDayExceptionView(updatedException);
    }

    @Transactional
    public void deleteException(UUID exceptionId){
        WorkDayExceptions exception = getExceptionOrThrow(exceptionId);

        repository.delete(exception);

        WorkDayExceptionDeletedEvent event = WorkDayExceptionDeletedEvent.builder()
                                                .userId(exception.getEmployee().getUserId())
                                                .exceptionId(exception.getId())
                                                .start(exception.getStartAt())
                                                .end(exception.getEndAt())
                                                .deletedAt(Instant.now())
                                                .build();
        kafkaProducerService.sendEvent(WORKDAY_EXCEPTION_TOPIC, event.getUserId().toString(), event);
        log.info("Иcключение в рабочих днях {} удалено",exception.getId());
    }
}

