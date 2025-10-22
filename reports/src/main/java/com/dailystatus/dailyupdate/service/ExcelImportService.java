package com.dailystatus.dailyupdate.service;

import com.dailystatus.dailyupdate.dto.ExcelRowDTO;
import com.dailystatus.dailyupdate.entity.DailyReport;
import com.dailystatus.dailyupdate.entity.DailyReportHistory;
import com.dailystatus.dailyupdate.entity.Employee;
import com.dailystatus.dailyupdate.repository.DailyReportHistoryRepository;
import com.dailystatus.dailyupdate.repository.DailyReportRepository;
import com.dailystatus.dailyupdate.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExcelImportService {

    private final EmployeeRepository employeeRepo;
    private final DailyReportRepository dailyRepo;
    private final DailyReportHistoryRepository historyRepo;

    public void importExcel(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int rowCount = 0;
        for (Row row : sheet) {
            if (rowCount == 0) { // skip header
                rowCount++;
                continue;
            }

            ExcelRowDTO dto = mapRowToDTO(row);
            processRow(dto);
            rowCount++;
        }

        workbook.close();
        fis.close();

        System.out.println("✅ Excel imported successfully with " + (rowCount - 1) + " records.");
    }

    private ExcelRowDTO mapRowToDTO(Row row) {
        ExcelRowDTO dto = new ExcelRowDTO();
        dto.setDate(row.getCell(0).getLocalDateTimeCellValue().toLocalDate());
        dto.setSprintNo(row.getCell(1).getStringCellValue());
        dto.setTicketNo(row.getCell(2).getStringCellValue());
        dto.setParentPc(row.getCell(3).getStringCellValue());
        dto.setResourceName(row.getCell(4).getStringCellValue());
        dto.setWorkPlanned(row.getCell(5).getStringCellValue());
        dto.setEstimation(BigDecimal.valueOf(row.getCell(6).getNumericCellValue()));
        dto.setStatus(row.getCell(7).getStringCellValue());
        dto.setActualTime(BigDecimal.valueOf(row.getCell(8).getNumericCellValue()));
        dto.setReasonForDelay(row.getCell(9).getStringCellValue());
        dto.setComments(row.getCell(10).getStringCellValue());
        return dto;
    }

    private void processRow(ExcelRowDTO dto) {
        Employee emp = employeeRepo.findByResourceName(dto.getResourceName())
                .orElseGet(() -> {
                    Employee e = new Employee();
                    e.setResourceName(dto.getResourceName());
                    return employeeRepo.save(e);
                });

        Optional<DailyReport> existing = dailyRepo.findByEmployeeAndReportDateAndTicketNo(
                emp, dto.getDate(), dto.getTicketNo());

        DailyReport report;
        if (existing.isPresent()) {
            report = existing.get();

            // Save old data into history
            DailyReportHistory history = new DailyReportHistory();
            BeanUtils.copyProperties(report, history);
            history.setReport(report);
            historyRepo.save(history);

            // Update existing record
            BeanUtils.copyProperties(dto, report);
            report.setEmployee(emp);
        } else {
            report = new DailyReport();
            BeanUtils.copyProperties(dto, report);
            report.setEmployee(emp);
        }

        dailyRepo.save(report);
    }
}
