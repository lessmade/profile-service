package com.worktime.profileservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.entity.WorkDayExceptions;
import com.worktime.profileservice.mapper.WorkDayExceptionMapper;
import com.worktime.profileservice.model.enums.WorkDayExceptionStatus;
import com.worktime.profileservice.model.request.WorkDayExceptionRequest;
import com.worktime.profileservice.model.response.VacationResponse;
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
    private final EmployeeProfileRepository employeeRepository;
    private final WorkDayExceptionMapper mapper;

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

        log.info("Заявкав на исключения в рабочих днях создана для сотрудника {}",employee.getId());

        return mapper.toWorkDayExceptionView(savedException);
    }

    @Transactional
    public WorkDayExceptionResponse approveException(UUID exceptionId){
        WorkDayExceptions exception = getExceptionOrThrow(exceptionId);
        
        exception.setStatus(WorkDayExceptionStatus.APPROVED);

        WorkDayExceptions updatedException = repository.save(exception);
        log.info("Исключения рабочих дней {} подтверждены", updatedException.getId());

        return mapper.toWorkDayExceptionView(updatedException);
    }
    @Transactional
    public WorkDayExceptionResponse rejectException(UUID exceptionId){
        WorkDayExceptions exception = getExceptionOrThrow(exceptionId);
        
        exception.setStatus(WorkDayExceptionStatus.REJECTED);

        WorkDayExceptions updatedException = repository.save(exception);
        log.info("Исключения рабочих дней {} отклонены", updatedException.getId());

        return mapper.toWorkDayExceptionView(updatedException);
    }

    @Transactional
    public WorkDayExceptionResponse cancelException(UUID exceptionId){
        WorkDayExceptions exception = getExceptionOrThrow(exceptionId);
        
        exception.setStatus(WorkDayExceptionStatus.CANCELLED);

        WorkDayExceptions updatedException = repository.save(exception);
        log.info("Исключения рабочих дней {} отменены", updatedException.getId());

        return mapper.toWorkDayExceptionView(updatedException);
    }

    @Transactional
    public void deleteException(UUID exceptionId){
        WorkDayExceptions exception = getExceptionOrThrow(exceptionId);

        repository.delete(exception);
        log.info("Иcключение в рабочих днях {} удалено",exception.getId());
    }
}

