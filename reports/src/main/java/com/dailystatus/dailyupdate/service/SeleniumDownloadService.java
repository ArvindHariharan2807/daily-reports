package com.dailystatus.dailyupdate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SeleniumDownloadService {

    @Value("${download.folder.path}")
    private String downloadPath;

    // Temporary: just return the existing Excel file
    public String downloadReport() {
        // Put your test Excel file name here
        File file = new File(downloadPath + "/report.xlsx");

        if (file.exists()) {
            System.out.println("✅ Using existing file: " + file.getAbsolutePath());
            return file.getAbsolutePath();
        } else {
            throw new RuntimeException("❌ Test Excel file not found at: " + file.getAbsolutePath());
        }
    }
}
