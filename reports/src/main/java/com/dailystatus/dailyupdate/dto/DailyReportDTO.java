package com.dailystatus.dailyupdate.dto;

public class DailyReportDTO {
    private int empId;
    private String empName;
    private String todayEntry;
    private String comment;

    // Getters and Setters
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }

    public String getTodayEntry() { return todayEntry; }
    public void setTodayEntry(String todayEntry) { this.todayEntry = todayEntry; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
