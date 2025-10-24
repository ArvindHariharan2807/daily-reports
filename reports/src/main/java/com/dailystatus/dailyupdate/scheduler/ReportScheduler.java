package com.dailystatus.dailyupdate.scheduler;

import com.dailystatus.dailyupdate.service.ExcelImportService;
import com.dailystatus.dailyupdate.service.ReportService;
import com.dailystatus.dailyupdate.service.SeleniumDownloadService;
import com.dailystatus.dailyupdate.service.SharePointDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportScheduler {

    private final SharePointDownloadService downloadService;
    private final ExcelImportService excelImportService;
    private final ReportService reportService;

    public ReportScheduler(SharePointDownloadService downloadService,
                           ExcelImportService excelImportService,
                           ReportService reportService) {
        this.downloadService = downloadService;
        this.excelImportService = excelImportService;
        this.reportService = reportService;
    }

    @Scheduled(cron = "0 59 11 * * *", zone = "Asia/Kolkata")
    public void importPlannedHours() {
        log.info("🌅 Morning Excel import (Planned Hours)");
        runExcelImport();
    }

    @Scheduled(cron = "0 0 20 * * *", zone = "Asia/Kolkata")
    public void importActualHours() {
        log.info("🌇 Evening Excel import (Actual Hours)");
        runExcelImport();
    }

    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Kolkata")
    public void moveToHistory() {
        log.info("🌙 Moving yesterday's data to history...");
        reportService.moveToHistory();
    }

    private void runExcelImport() {
        try {
            String filePath = downloadService.downloadReport();
            excelImportService.importExcel(filePath);
        } catch (Exception e) {
            log.error("❌ Excel import failed: {}", e.getMessage(), e);
        }
    }
}
