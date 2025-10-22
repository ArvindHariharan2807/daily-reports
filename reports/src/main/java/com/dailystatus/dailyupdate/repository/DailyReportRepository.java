package com.dailystatus.dailyupdate.repository;

import com.dailystatus.dailyupdate.entity.DailyReport;
import com.dailystatus.dailyupdate.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    Optional<DailyReport> findByEmployeeAndReportDateAndTicketNo(Employee employee, LocalDate reportDate, String ticketNo);
    List<DailyReport> findByEmployee(Employee employee);
}
