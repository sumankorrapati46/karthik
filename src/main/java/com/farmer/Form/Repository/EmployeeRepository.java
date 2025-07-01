package com.farmer.Form.Repository;

import com.farmer.Form.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // No extra methods needed for basic CRUD
}
