package com.suryaconstruction.permit.dto;

import java.time.LocalDate;

public class PermitEntryDto {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate entryDate;
    private String hotWorkActivity;
    private String heightWorkActivity;
    private String generalWorkActivity;
    private String manpowerNames;

    public PermitEntryDto() {
    }

    public PermitEntryDto(Long id, Long employeeId, String employeeName, LocalDate entryDate,
                          String hotWorkActivity, String heightWorkActivity, String generalWorkActivity, String manpowerNames) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.entryDate = entryDate;
        this.hotWorkActivity = hotWorkActivity;
        this.heightWorkActivity = heightWorkActivity;
        this.generalWorkActivity = generalWorkActivity;
        this.manpowerNames = manpowerNames;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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
}
