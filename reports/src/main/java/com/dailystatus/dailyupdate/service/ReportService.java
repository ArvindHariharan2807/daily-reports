package com.dailystatus.dailyupdate.service;

import com.dailystatus.dailyupdate.entity.DailyReport;
import com.dailystatus.dailyupdate.entity.DailyReportHistory;
import com.dailystatus.dailyupdate.repository.DailyReportHistoryRepository;
import com.dailystatus.dailyupdate.repository.DailyReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final DailyReportRepository dailyReportRepository;
    private final DailyReportHistoryRepository historyRepository;

    public ReportService(DailyReportRepository dailyReportRepository,
                         DailyReportHistoryRepository historyRepository) {
        this.dailyReportRepository = dailyReportRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    public void moveToHistory() {
        // Fetch all daily reports to move
        List<DailyReport> reports = dailyReportRepository.findAll();

        for (DailyReport report : reports) {
            // Create history object
            DailyReportHistory history = new DailyReportHistory();

            history.setEmployeeName(report.getEmployeeName());
            history.setReportDate(report.getReportDate());
            history.setSprintNo(report.getSprintNo() != null ? report.getSprintNo() : "");
            history.setTicketNo(report.getTicketNo() != null ? report.getTicketNo() : "");
            history.setParentPc(report.getParentPc() != null ? report.getParentPc() : "");
            history.setWorkPlanned(report.getWorkPlanned() != null ? report.getWorkPlanned() : "");
            history.setEstimation(report.getEstimation() != null ? report.getEstimation() : BigDecimal.valueOf(0.0));
            history.setStatus(report.getStatus() != null ? report.getStatus() : "");
            history.setActualTime(report.getActualTime() != null ? report.getActualTime() : BigDecimal.valueOf(0.0));
            history.setReasonForDelay(report.getReasonForDelay() != null ? report.getReasonForDelay() : "");
            history.setComments(report.getComments() != null ? report.getComments() : "");

            // Save to history table
            historyRepository.save(history);

            // Delete the report after successful history save
            dailyReportRepository.delete(report);
        }
    }
}
