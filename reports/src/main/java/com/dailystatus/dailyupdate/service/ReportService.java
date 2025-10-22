package com.dailystatus.dailyupdate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private com.dailystatus.dailyupdate.service.SeleniumDownloadService seleniumDownloadService;

    @Autowired
    private ExcelImportService excelImportService;

    public void downloadAndProcessReport() throws Exception {
        // Step 1️⃣: Use Selenium to download the Google Sheet
        String downloadedFilePath = seleniumDownloadService.downloadReport();

        // Step 2️⃣: Parse the Excel and process it
        excelImportService.importExcel(downloadedFilePath);

        System.out.println("✅ Report downloaded and processed successfully.");
    }
}
