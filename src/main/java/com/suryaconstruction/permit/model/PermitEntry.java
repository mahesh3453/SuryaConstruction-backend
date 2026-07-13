package com.suryaconstruction.permit.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "permit_entries", 
       uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "entry_date"})})
public class PermitEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "hot_work_activity", columnDefinition = "TEXT")
    private String hotWorkActivity;

    @Column(name = "height_work_activity", columnDefinition = "TEXT")
    private String heightWorkActivity;

    @Column(name = "general_work_activity", columnDefinition = "TEXT")
    private String generalWorkActivity;

    @Column(name = "manpower_names", columnDefinition = "TEXT")
    private String manpowerNames;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PermitEntry() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getHotWorkActivity() {
        return hotWorkActivity;
    }

    public void setHotWorkActivity(String hotWorkActivity) {
        this.hotWorkActivity = hotWorkActivity;
    }

    public String getHeightWorkActivity() {
        return heightWorkActivity;
    }

    public void setHeightWorkActivity(String heightWorkActivity) {
        this.heightWorkActivity = heightWorkActivity;
    }

    public String getGeneralWorkActivity() {
        return generalWorkActivity;
    }

    public void setGeneralWorkActivity(String generalWorkActivity) {
        this.generalWorkActivity = generalWorkActivity;
    }

    public String getManpowerNames() {
        return manpowerNames;
    }

    public void setManpowerNames(String manpowerNames) {
        this.manpowerNames = manpowerNames;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
