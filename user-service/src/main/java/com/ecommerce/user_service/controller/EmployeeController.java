package com.ecommerce.user_service.controller;


import com.ecommerce.user_service.dto.PasswordChangeDTO;
import com.ecommerce.user_service.model.Employee;
import com.ecommerce.user_service.service.EmployeeService;
import com.ecommerce.user_service.service.WarehouseVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WarehouseVerificationService warehouseVerificationService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        Optional<Employee> existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee.isPresent()) {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/password/{id}")
    public ResponseEntity<?> updateEmployeePassword(@PathVariable String id, @RequestBody PasswordChangeDTO passwordChangeRequest) {
        Optional<Employee> existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee.isPresent()) {
            Employee  updatedEmployee= employeeService.updateEmployeePassword(id, passwordChangeRequest);
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/verify/{id}")
    public ResponseEntity<?> verifyWarehouse(@PathVariable Long id) {
        boolean exists = warehouseVerificationService.verifyWarehouseExists(id);
        return ResponseEntity.ok(exists);
    }
}
