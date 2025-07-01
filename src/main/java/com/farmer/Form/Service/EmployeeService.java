package com.farmer.Form.Service;

import com.farmer.Form.Entity.Employee;
import java.util.List;

public interface EmployeeService {

    // Save a new or updated employee
    Employee saveEmployee(Employee employee);

    // Get all employees
    List<Employee> getAllEmployees();

    // Get single employee by ID
    Employee getEmployeeById(Long id);

    // Delete employee by ID
    void deleteEmployee(Long id);
}
