package com.suryaconstruction.permit.controller;

import com.suryaconstruction.permit.dto.PermitEntryDto;
import com.suryaconstruction.permit.service.ExcelExportService;
import com.suryaconstruction.permit.service.PermitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/permits")
public class AdminPermitController {

    @Autowired
    private PermitService permitService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public ResponseEntity<List<PermitEntryDto>> getEntriesByDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate queryDate = date != null ? date : LocalDate.now();
        return ResponseEntity.ok(permitService.getEntriesByDate(queryDate));
    }

    @GetMapping("/history")
    public ResponseEntity<List<PermitEntryDto>> getEmployeeHistory(
            @RequestParam Long employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(permitService.getHistoryByEmployee(employeeId, startDate, endDate));
    }

    @PostMapping
    public ResponseEntity<PermitEntryDto> createOrUpdateEntry(@RequestBody PermitEntryDto dto) {
        if (dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        return ResponseEntity.ok(permitService.createOrUpdateEntry(dto.getEmployeeId(), dto, true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermitEntryDto> updateEntry(@PathVariable Long id, @RequestBody PermitEntryDto dto) {
        return ResponseEntity.ok(permitService.updateEntryById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEntry(@PathVariable Long id) {
        permitService.deleteEntry(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Permit entry deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws IOException {
        
        List<PermitEntryDto> entries = permitService.getEntriesByDate(date);
        byte[] excelBytes = excelExportService.exportToExcel(date, entries);

        String filename = "permit_details_" + date + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
