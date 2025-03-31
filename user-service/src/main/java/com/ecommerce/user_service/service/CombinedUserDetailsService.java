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
    private final CustomUserDetailsService customerUserDetailsService;
    private final EmployeeUserDetailsService employeeUserDetailsService;

    @Autowired
    public CombinedUserDetailsService(
            CustomUserDetailsService customerUserDetailsService,
            EmployeeUserDetailsService employeeUserDetailsService) {
        this.customerUserDetailsService = customerUserDetailsService;
        this.employeeUserDetailsService = employeeUserDetailsService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return customerUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            try {
                return employeeUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException ex) {
                throw new UsernameNotFoundException("User not found in either repository: " + username);
            }
        }
    }
}