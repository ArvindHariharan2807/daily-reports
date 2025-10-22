package com.dailystatus.dailyupdate.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class ExcelRowDTO {
    private LocalDate date;
    private String sprintNo;
    private String ticketNo;
    private String parentPc;
    private String resourceName;
    private String workPlanned;
    private BigDecimal estimation;
    private String status;
    private BigDecimal actualTime;
    private String reasonForDelay;
    private String comments;

}
