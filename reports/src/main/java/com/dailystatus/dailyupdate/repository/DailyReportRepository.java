package com.dailystatus.dailyupdate.repository;

import com.dailystatus.dailyupdate.entity.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {

    // For rows with ticket numbers (unique)
    Optional<DailyReport> findByEmployeeNameAndTicketNoAndReportDate(
            String employeeName, String ticketNo, LocalDate reportDate);

    // For rows without ticket numbers (multiple possible)
    List<DailyReport> findByEmployeeNameAndReportDate(
            String employeeName, LocalDate reportDate);

    // Delete only today's records
    void deleteByReportDate(LocalDate reportDate);
}
