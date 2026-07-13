package com.suryaconstruction.permit.service;

import com.suryaconstruction.permit.dto.EmployeeDto;
import com.suryaconstruction.permit.exception.BadRequestException;
import com.suryaconstruction.permit.exception.ResourceNotFoundException;
import com.suryaconstruction.permit.model.Employee;
import com.suryaconstruction.permit.model.Role;
import com.suryaconstruction.permit.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return convertToDto(employee);
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Password cannot be empty");
        }
        if (employeeRepository.existsByUsername(dto.getUsername().trim())) {
            throw new BadRequestException("Username is already taken: " + dto.getUsername());
        }

        Employee employee = new Employee(
                dto.getName().trim(),
                dto.getUsername().trim().toLowerCase(),
                dto.getPassword(),
                dto.getRole() != null ? dto.getRole() : Role.EMPLOYEE
        );
        Employee saved = employeeRepository.save(employee);
        return convertToDto(saved);
    }

    @Transactional
    public EmployeeDto updateEmployee(Long id, EmployeeDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        String newUsername = dto.getUsername().trim().toLowerCase();
        if (!employee.getUsername().equals(newUsername)) {
            if (employeeRepository.existsByUsername(newUsername)) {
                throw new BadRequestException("Username is already taken: " + newUsername);
            }
            employee.setUsername(newUsername);
        }

        employee.setName(dto.getName().trim());
        if (dto.getRole() != null) {
            employee.setRole(dto.getRole());
        }
        // Plain text updates are only when password is provided in employee edit or separate reset
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            employee.setPassword(dto.getPassword());
        }

        Employee updated = employeeRepository.save(employee);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Transactional
    public void changePassword(Long employeeId, String oldPassword, String newPassword) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (!employee.getPassword().equals(oldPassword)) {
            throw new BadRequestException("Incorrect current password");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BadRequestException("New password cannot be empty");
        }

        employee.setPassword(newPassword);
        employeeRepository.save(employee);
    }

    @Transactional
    public void resetPassword(Long id, String newPassword) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BadRequestException("New password cannot be empty");
        }

        employee.setPassword(newPassword);
        employeeRepository.save(employee);
    }

    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setUsername(employee.getUsername());
        // For security or simplicity, do not return password in list view.
        dto.setPassword(null); 
        dto.setRole(employee.getRole());
        return dto;
    }
}
