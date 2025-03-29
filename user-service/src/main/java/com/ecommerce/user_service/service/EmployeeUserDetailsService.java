package com.ecommerce.user_service.service;

import com.ecommerce.user_service.model.Employee;
import com.ecommerce.user_service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with email: " + email));

        String password = employee.getPassword() == null ? "{noop}OAUTH_USER" : employee.getPassword();

        return org.springframework.security.core.userdetails.User
                .withUsername(employee.getEmail())
                .password(password)
                .roles(employee.getRole())
                .build();
    }
}