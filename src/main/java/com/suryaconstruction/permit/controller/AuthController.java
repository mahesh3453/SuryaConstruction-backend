package com.suryaconstruction.permit.controller;

import com.suryaconstruction.permit.dto.LoginRequest;
import com.suryaconstruction.permit.dto.PasswordChangeRequest;
import com.suryaconstruction.permit.exception.UnauthorizedException;
import com.suryaconstruction.permit.model.Employee;
import com.suryaconstruction.permit.repository.EmployeeRepository;
import com.suryaconstruction.permit.security.SessionUser;
import com.suryaconstruction.permit.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<SessionUser> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Employee employee = employeeRepository.findByUsername(loginRequest.getUsername().trim().toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        // Direct plain text check
        if (!employee.getPassword().equals(loginRequest.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        // Create session
        HttpSession session = request.getSession(true);
        SessionUser sessionUser = new SessionUser(
                employee.getId(),
                employee.getName(),
                employee.getUsername(),
                employee.getRole()
        );
        session.setAttribute("user", sessionUser);

        return ResponseEntity.ok(sessionUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<SessionUser> me(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new UnauthorizedException("Not authenticated");
        }
        SessionUser user = (SessionUser) session.getAttribute("user");
        return ResponseEntity.ok(user);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody PasswordChangeRequest changeRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new UnauthorizedException("Not authenticated");
        }
        SessionUser user = (SessionUser) session.getAttribute("user");

        employeeService.changePassword(user.getId(), changeRequest.getOldPassword(), changeRequest.getNewPassword());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }
}
