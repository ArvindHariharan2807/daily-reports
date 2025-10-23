package com.dailystatus.dailyupdate.service;

import com.dailystatus.dailyupdate.entity.DailyReport;
import com.dailystatus.dailyupdate.repository.DailyReportRepository;
import com.dailystatus.dailyupdate.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Optional;

@Slf4j
@Service
public class ExcelImportService {

    @Autowired
    private DailyReportRepository dailyReportRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void importExcel(String excelFilePath) {
        LocalDate today = LocalDate.now();
        File excelFile = new File(excelFilePath);

        log.info("🧹 Deleting existing records for {}", today);
        dailyReportRepository.deleteByReportDate(today);

        List<DailyReport> excelRows = parseExcel(excelFile, today);

        int skippedCount = 0;
        int importedCount = 0;

        for (DailyReport row : excelRows) {
            // ✅ Validate employee existence before inserting
            String empName = row.getEmployeeName();
            if (empName == null || empName.isBlank()) continue;

            boolean employeeExists = employeeRepository.findByResourceNameIgnoreCase(empName.trim()).isPresent();
            if (!employeeExists) {
                log.debug("🚫 Skipping record — employee not found in EMPLOYEE table: {}", empName);
                skippedCount++;
                continue;
            }

            // ✅ Only insert/update today's records
            if (!today.equals(row.getReportDate())) continue;

            if (row.getTicketNo() != null && !row.getTicketNo().isBlank()) {
                Optional<DailyReport> existing = dailyReportRepository
                        .findByEmployeeNameAndTicketNoAndReportDate(
                                row.getEmployeeName(), row.getTicketNo(), today);

                if (existing.isPresent()) {
                    DailyReport report = existing.get();
                    updateReport(report, row);
                    dailyReportRepository.save(report);
                } else {
                    dailyReportRepository.save(row);
                }
            } else {
                dailyReportRepository.save(row);
            }
            importedCount++;
        }

        log.info("✅ Import complete. {} records saved, {} skipped (employees not found).", importedCount, skippedCount);
    }

    private void updateReport(DailyReport target, DailyReport source) {
        target.setWorkPlanned(source.getWorkPlanned());
        target.setEstimation(source.getEstimation());
        target.setActualTime(source.getActualTime());
        target.setStatus(source.getStatus());
        target.setReasonForDelay(source.getReasonForDelay());
        target.setComments(source.getComments());
    }

    private List<DailyReport> parseExcel(File excelFile, LocalDate today) {
        List<DailyReport> reports = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                String dateStr = getCellValue(row.getCell(0));
                if (dateStr == null || dateStr.isBlank()) continue;

                LocalDate reportDate;
                try {
                    reportDate = LocalDate.parse(dateStr.trim(), df);
                } catch (Exception e) {
                    log.warn("⏭️ Skipping invalid date at row {}: {}", row.getRowNum(), dateStr);
                    continue;
                }

                if (!reportDate.equals(today)) continue;

                DailyReport report = new DailyReport();
                report.setReportDate(reportDate);
                report.setSprintNo(getCellValue(row.getCell(1)));
                report.setTicketNo(getCellValue(row.getCell(2)));
                report.setParentPc(getCellValue(row.getCell(3)));
                report.setEmployeeName(getCellValue(row.getCell(4)));
                report.setWorkPlanned(getCellValue(row.getCell(5)));
                report.setEstimation(getBigDecimalCellValue(row.getCell(6)));
                report.setStatus(getCellValue(row.getCell(7)));
                report.setActualTime(getBigDecimalCellValue(row.getCell(8)));
                report.setReasonForDelay(getCellValue(row.getCell(9)));
                report.setComments(getCellValue(row.getCell(10)));

                reports.add(report);
            }

        } catch (Exception e) {
            log.error("❌ Error reading Excel file: {}", e.getMessage(), e);
        }

        return reports;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    : String.valueOf((long) cell.getNumericCellValue());
            default -> null;
        };
    }

    private BigDecimal getBigDecimalCellValue(Cell cell) {
        if (cell == null) return null;
        try {
            return switch (cell.getCellType()) {
                case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING -> {
                    String val = cell.getStringCellValue().trim();
                    yield val.isEmpty() ? null : new BigDecimal(val);
                }
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }
}
