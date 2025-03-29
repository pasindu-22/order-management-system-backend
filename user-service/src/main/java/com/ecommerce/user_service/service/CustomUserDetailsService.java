package com.ecommerce.user_service.service;

import com.ecommerce.user_service.model.Employee;
import com.ecommerce.user_service.model.User;
import com.ecommerce.user_service.repository.EmployeeRepository;
import com.ecommerce.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Use a placeholder value for OAuth users who don't have a password
        String password = user.getPassword() == null ? "{noop}OAUTH_USER" : user.getPassword();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(password)
                .roles(user.getRole())
                .build();
    }

    public UserDetails loadEmployeeByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with email: " + username));

        // Use a placeholder value for OAuth users who don't have a password
        String password = employee.getPassword() == null ? "{noop}OAUTH_USER" : employee.getPassword();

        return org.springframework.security.core.userdetails.User
                .withUsername(employee.getEmail())
                .password(password)
                .roles(employee.getRole())
                .build();
    }
}