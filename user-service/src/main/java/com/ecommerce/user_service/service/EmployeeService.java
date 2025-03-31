package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.PasswordChangeDTO;
import com.ecommerce.user_service.model.Employee;
import com.ecommerce.user_service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword())); // Hash password
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    public Employee updateEmployee(String id, Employee employee) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee updatedEmployee = existingEmployee.get();
            updatedEmployee.setFirstName(employee.getFirstName());
            updatedEmployee.setLastName(employee.getLastName());
            updatedEmployee.setEmail(employee.getEmail());
            updatedEmployee.setPhoneNumber(employee.getPhoneNumber());
            updatedEmployee.setAbout(employee.getAbout());
            updatedEmployee.setAddress(employee.getAddress());
            return employeeRepository.save(updatedEmployee);
        } else {
            return null; // or throw an exception
        }
    }

    public Employee updateEmployeePassword(String id, PasswordChangeDTO passwordChangeRequest) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee updatedEmployee = existingEmployee.get();
            if (passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(), updatedEmployee.getPassword())) {
                updatedEmployee.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword())); // Hash new password
                return employeeRepository.save(updatedEmployee);
            } else {
                return null; // or throw an exception
            }
        } else {
            return null; // or throw an exception
        }
    }
}