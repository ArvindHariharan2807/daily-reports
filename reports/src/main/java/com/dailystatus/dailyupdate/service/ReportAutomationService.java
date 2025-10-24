package com.dailystatus.dailyupdate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.File;

@Slf4j
@Service
public class ReportAutomationService {

    private final SharePointDownloadService sharePointDownloadService;
    private final ExcelImportService excelImportService;

    public ReportAutomationService(SharePointDownloadService sharePointDownloadService,
                                   ExcelImportService excelImportService) {
        this.sharePointDownloadService = sharePointDownloadService;
        this.excelImportService = excelImportService;
    }

    public void runAutomation() {
        try {
            log.info("🚀 Starting full report automation flow...");

            // 1️⃣ Download the Excel from SharePoint
            String downloadedFilePath = sharePointDownloadService.downloadReport();
            log.info("📥 File downloaded successfully at: {}", downloadedFilePath);

            // 2️⃣ Import the data from the downloaded file
            excelImportService.importExcel(downloadedFilePath);

            log.info("✅ Report automation completed successfully.");

        } catch (Exception e) {
            log.error("❌ Report automation failed: {}", e.getMessage(), e);
        }
    }
}
