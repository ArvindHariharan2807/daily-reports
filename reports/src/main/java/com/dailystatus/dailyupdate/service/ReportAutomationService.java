package com.dailystatus.dailyupdate.service;

import org.springframework.stereotype.Service;

@Service
public class ReportAutomationService {

    private final ExcelImportService excelImportService;

    public ReportAutomationService(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    public void runAutomation() {
        try {
            System.out.println("🚀 Starting report automation...");

            // ✅ Directly use the local Excel file for testing
            String filePath = "C:\\daily-reports\\downloads\\report.xlsx";
            excelImportService.importExcel(filePath);

            System.out.println("✅ Report automation completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Report automation failed: " + e.getMessage());
        }
    }
}
