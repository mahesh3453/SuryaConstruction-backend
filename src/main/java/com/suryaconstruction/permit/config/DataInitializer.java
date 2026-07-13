package com.suryaconstruction.permit.config;

import com.suryaconstruction.permit.model.Employee;
import com.suryaconstruction.permit.model.Role;
import com.suryaconstruction.permit.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Admin
        if (!employeeRepository.existsByUsername("admin")) {
            Employee admin = new Employee("Administrator", "admin", "admin123", Role.ADMIN);
            employeeRepository.save(admin);
            System.out.println("Seeded Admin account (username: admin, password: admin123)");
        }

        // 2. Seed Employees
        List<EmployeeSeedInfo> defaultEmployees = new ArrayList<>();
        defaultEmployees.add(new EmployeeSeedInfo("Kushal Pawar", "kushal.pawar"));
        defaultEmployees.add(new EmployeeSeedInfo("Ganesh Lahane", "ganesh.lahane"));
        defaultEmployees.add(new EmployeeSeedInfo("Juned Sheikh", "juned.sheikh"));
        defaultEmployees.add(new EmployeeSeedInfo("Ganesh Adhane", "ganesh.adhane"));
        defaultEmployees.add(new EmployeeSeedInfo("Babasaheb Ahire", "babasaheb.ahire"));
        defaultEmployees.add(new EmployeeSeedInfo("Radhakishan Waghmare", "radhakishan.waghmare"));
        defaultEmployees.add(new EmployeeSeedInfo("Rupnarayan Sharma", "rupnarayan.sharma"));

        for (EmployeeSeedInfo emp : defaultEmployees) {
            if (!employeeRepository.existsByUsername(emp.username)) {
                Employee employee = new Employee(emp.name, emp.username, "Surya@123", Role.EMPLOYEE);
                employeeRepository.save(employee);
                System.out.println("Seeded Employee account (name: " + emp.name + ", username: " + emp.username + ")");
            }
        }
    }

    private static class EmployeeSeedInfo {
        String name;
        String username;

        EmployeeSeedInfo(String name, String username) {
            this.name = name;
            this.username = username;
        }
    }
}
