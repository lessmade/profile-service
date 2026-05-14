package com.worktime.profileservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worktime.profileservice.entity.EmployeeProfile;
import com.worktime.profileservice.entity.Vacation;
import com.worktime.profileservice.mapper.VacationMapper;
import com.worktime.profileservice.model.enums.VacationStatus;
import com.worktime.profileservice.model.request.VacationRequest;
import com.worktime.profileservice.model.response.VacationResponse;
import com.worktime.profileservice.repository.EmployeeProfileRepository;
import com.worktime.profileservice.repository.VacationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacationService {

    private final VacationRepository repository;

    private final EmployeeProfileRepository employeeRepository;

    private final VacationMapper mapper;


    private Vacation getVacationOrThrow(UUID vacationId) {
    return repository.findById(vacationId)
            .orElseThrow(() -> new RuntimeException("Отпуск не найден"));
}

    @Transactional(readOnly = true)
    public List<VacationResponse> getAllVacations (){

        return repository.findAll()
                .stream()
                .map(mapper::toVacationView)
                .toList();

    }

    @Transactional(readOnly = true)
    public VacationResponse getVacationById(UUID vacationId){
        Vacation vacation = getVacationOrThrow(vacationId);

        return mapper.toVacationView(vacation);
    }
    
    @Transactional(readOnly = true)
    public List<VacationResponse> getEmployeeVacations(UUID employeeId){

        return repository.findByEmployee_Id(employeeId)
                .stream()
                .map(mapper::toVacationView)
                .toList();
    }

    @Transactional
    public VacationResponse approveVacation(UUID vacationId){
        Vacation vacation = getVacationOrThrow(vacationId);
        
        vacation.setStatus(VacationStatus.APPROVED);

        Vacation updatedVacation = repository.save(vacation);
        log.info("Отпуск {} одобрен", updatedVacation.getId());

        return mapper.toVacationView(updatedVacation);
    }
    
    @Transactional
    public VacationResponse rejectVacation(UUID vacationId){
        Vacation vacation = getVacationOrThrow(vacationId);
        
        vacation.setStatus(VacationStatus.REJECTED);

        Vacation updatedVacation = repository.save(vacation);
        log.info("Отпуск {} отклонен", updatedVacation.getId());

        return mapper.toVacationView(updatedVacation);
    }

    @Transactional
    public VacationResponse cancelVacation(UUID vacationId){
        Vacation vacation = getVacationOrThrow(vacationId);
        
        vacation.setStatus(VacationStatus.CANCELLED);

        Vacation updatedVacation = repository.save(vacation);
        log.info("Отпуск {} отменен", updatedVacation.getId());

        return mapper.toVacationView(updatedVacation);
    }

    @Transactional
    public VacationResponse createVacation(VacationRequest request){
        EmployeeProfile employee = employeeRepository.findById(request.getEmployeeId())
                                    .orElseThrow(()-> new RuntimeException("Сотрудник не найден"));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("Дата окончания раньше даты начала");

        }
        Vacation vacation = mapper.toEntity(request, employee);
        Vacation savedVacation = repository.save(vacation);
        
        log.info("Заявка на отпуск создана для сотрудника с id {}",employee.getId());

        return mapper.toVacationView(savedVacation);
    }
}


    
    

