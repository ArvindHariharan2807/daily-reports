package com.dailystatus.dailyupdate.service;

import org.springframework.stereotype.Service;

@Service
public class ReportAutomationService {

    private final SeleniumDownloadService seleniumDownloadService;
    private final ExcelImportService excelImportService;

    public ReportAutomationService(SeleniumDownloadService seleniumDownloadService,
                                   ExcelImportService excelImportService) {
        this.seleniumDownloadService = seleniumDownloadService;
        this.excelImportService = excelImportService;
    }

    public void runAutomation() {
        try {
            System.out.println("🚀 Starting report automation...");
            String filePath = seleniumDownloadService.downloadReport();
            excelImportService.importExcel(filePath);
            System.out.println("✅ Report automation completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Report automation failed: " + e.getMessage());
        }
    }
}
