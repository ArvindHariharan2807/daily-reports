package com.dailystatus.dailyupdate.repository;

import com.dailystatus.dailyupdate.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByResourceName(String resourceName);

    Optional<Employee> findByResourceNameIgnoreCase(String resourceName);

}
