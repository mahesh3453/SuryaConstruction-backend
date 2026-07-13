package com.suryaconstruction.permit.repository;

import com.suryaconstruction.permit.model.PermitEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermitEntryRepository extends JpaRepository<PermitEntry, Long> {
    Optional<PermitEntry> findByEmployeeIdAndEntryDate(Long employeeId, LocalDate entryDate);
    List<PermitEntry> findByEntryDate(LocalDate entryDate);
    List<PermitEntry> findByEmployeeIdOrderByEntryDateDesc(Long employeeId);
    List<PermitEntry> findByEmployeeIdAndEntryDateBetweenOrderByEntryDateDesc(Long employeeId, LocalDate startDate, LocalDate endDate);
}
