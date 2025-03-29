package com.ecommerce.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CombinedUserDetailsService implements UserDetailsService {
    private final CustomUserDetailsService customerService;
    private final EmployeeUserDetailsService employeeService;

    @Autowired
    public CombinedUserDetailsService(
            CustomUserDetailsService customerService,
            EmployeeUserDetailsService employeeService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return customerService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            try {
                return employeeService.loadUserByUsername(username);
            } catch (UsernameNotFoundException ex) {
                throw new UsernameNotFoundException("User not found in either repository: " + username);
            }
        }
    }
}