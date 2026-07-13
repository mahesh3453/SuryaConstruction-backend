package com.suryaconstruction.permit.service;

import com.suryaconstruction.permit.dto.PermitEntryDto;
import com.suryaconstruction.permit.exception.BadRequestException;
import com.suryaconstruction.permit.exception.ResourceNotFoundException;
import com.suryaconstruction.permit.model.Employee;
import com.suryaconstruction.permit.model.PermitEntry;
import com.suryaconstruction.permit.repository.EmployeeRepository;
import com.suryaconstruction.permit.repository.PermitEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermitService {

    @Autowired
    private PermitEntryRepository permitEntryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public PermitEntryDto getEntryByEmployeeAndDate(Long employeeId, LocalDate date) {
        return permitEntryRepository.findByEmployeeIdAndEntryDate(employeeId, date)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Transactional
    public PermitEntryDto createOrUpdateEntry(Long employeeId, PermitEntryDto dto, boolean isAdmin) {
        LocalDate entryDate = dto.getEntryDate() != null ? dto.getEntryDate() : LocalDate.now();

        // Enforce employee cutoff rule: employees can only write/edit for today's date
        if (!isAdmin && !entryDate.equals(LocalDate.now())) {
            throw new BadRequestException("Employees are only permitted to submit or edit entries for the current date (" + LocalDate.now() + ").");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        Optional<PermitEntry> existingOpt = permitEntryRepository.findByEmployeeIdAndEntryDate(employeeId, entryDate);
        PermitEntry entry;
        
        if (existingOpt.isPresent()) {
            entry = existingOpt.get();
        } else {
            entry = new PermitEntry();
            entry.setEmployee(employee);
            entry.setEntryDate(entryDate);
        }

        entry.setHotWorkActivity(dto.getHotWorkActivity());
        entry.setHeightWorkActivity(dto.getHeightWorkActivity());
        entry.setGeneralWorkActivity(dto.getGeneralWorkActivity());
        entry.setManpowerNames(dto.getManpowerNames());

        PermitEntry saved = permitEntryRepository.save(entry);
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PermitEntryDto> getEntriesByDate(LocalDate date) {
        return permitEntryRepository.findByEntryDate(date)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PermitEntryDto> getHistoryByEmployee(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<PermitEntry> entries;
        if (startDate != null && endDate != null) {
            entries = permitEntryRepository.findByEmployeeIdAndEntryDateBetweenOrderByEntryDateDesc(employeeId, startDate, endDate);
        } else {
            entries = permitEntryRepository.findByEmployeeIdOrderByEntryDateDesc(employeeId);
        }
        return entries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PermitEntryDto updateEntryById(Long id, PermitEntryDto dto) {
        PermitEntry entry = permitEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permit entry not found with id: " + id));

        LocalDate targetDate = dto.getEntryDate() != null ? dto.getEntryDate() : entry.getEntryDate();
        Long employeeId = entry.getEmployee().getId();

        Optional<PermitEntry> existingOpt = permitEntryRepository.findByEmployeeIdAndEntryDate(employeeId, targetDate);

        if (existingOpt.isPresent() && !existingOpt.get().getId().equals(id)) {
            PermitEntry existing = existingOpt.get();
            existing.setHotWorkActivity(dto.getHotWorkActivity());
            existing.setHeightWorkActivity(dto.getHeightWorkActivity());
            existing.setGeneralWorkActivity(dto.getGeneralWorkActivity());
            existing.setManpowerNames(dto.getManpowerNames());
            permitEntryRepository.delete(entry);
            PermitEntry saved = permitEntryRepository.save(existing);
            return convertToDto(saved);
        }

        entry.setHotWorkActivity(dto.getHotWorkActivity());
        entry.setHeightWorkActivity(dto.getHeightWorkActivity());
        entry.setGeneralWorkActivity(dto.getGeneralWorkActivity());
        entry.setManpowerNames(dto.getManpowerNames());

        if (dto.getEntryDate() != null) {
            entry.setEntryDate(dto.getEntryDate());
        }

        PermitEntry saved = permitEntryRepository.save(entry);
        return convertToDto(saved);
    }

    @Transactional
    public void deleteEntry(Long id) {
        if (!permitEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Permit entry not found with id: " + id);
        }
        permitEntryRepository.deleteById(id);
    }

    private PermitEntryDto convertToDto(PermitEntry entry) {
        return new PermitEntryDto(
                entry.getId(),
                entry.getEmployee().getId(),
                entry.getEmployee().getName(),
                entry.getEntryDate(),
                entry.getHotWorkActivity(),
                entry.getHeightWorkActivity(),
                entry.getGeneralWorkActivity(),
                entry.getManpowerNames()
        );
    }
}
