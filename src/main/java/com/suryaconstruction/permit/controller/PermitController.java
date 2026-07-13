package com.suryaconstruction.permit.controller;

import com.suryaconstruction.permit.dto.PermitEntryDto;
import com.suryaconstruction.permit.security.SessionUser;
import com.suryaconstruction.permit.service.PermitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/permits")
public class PermitController {

    @Autowired
    private PermitService permitService;

    @GetMapping("/by-date")
    public ResponseEntity<PermitEntryDto> getEntryByDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        SessionUser user = (SessionUser) session.getAttribute("user");

        LocalDate queryDate = date != null ? date : LocalDate.now();
        PermitEntryDto entry = permitService.getEntryByEmployeeAndDate(user.getId(), queryDate);
        return ResponseEntity.ok(entry);
    }

    @PostMapping
    public ResponseEntity<PermitEntryDto> createOrUpdateEntry(
            @RequestBody PermitEntryDto dto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        SessionUser user = (SessionUser) session.getAttribute("user");

        // Force employee ID to be the logged-in user ID
        PermitEntryDto saved = permitService.createOrUpdateEntry(user.getId(), dto, false);
        return ResponseEntity.ok(saved);
    }
}
