package com.dailystatus.dailyupdate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private LocalDate reportDate;
    private String sprintNo;
    private String ticketNo;
    private String parentPc;
    private String workPlanned;
    private BigDecimal estimation;
    private String status;
    private BigDecimal actualTime;
    private String reasonForDelay;
    @Lob
    private String comments;
    private LocalDateTime lastUpdated = LocalDateTime.now();

    // Store employee name directly
    private String employeeName;

    @Transient
    private String resourceName; // Used only for Excel import
}
