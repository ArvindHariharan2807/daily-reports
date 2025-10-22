package com.dailystatus.dailyupdate.scheduler;

import com.dailystatus.dailyupdate.service.ReportAutomationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportScheduler {

    private final ReportAutomationService automationService;

    public ReportScheduler(ReportAutomationService automationService) {
        this.automationService = automationService;
    }

    // Run daily at 9 PM IST (adjust cron if needed)
    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Kolkata")
    public void scheduleDailyReportDownload() {
        automationService.runAutomation();
    }
}
