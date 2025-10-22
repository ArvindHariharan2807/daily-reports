package com.dailystatus.dailyupdate.controller;

import com.dailystatus.dailyupdate.service.ExcelImportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ExcelImportService excelImportService;

    public ReportController(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    @PostMapping("/import")
    public String importExcel(@RequestParam String filePath) throws Exception {
        excelImportService.importExcel(filePath);
        return "Excel imported successfully!";
    }
}
