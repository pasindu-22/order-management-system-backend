package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.security.JwtUtil;
import com.ecommerce.user_service.service.CustomUserDetailsService;
import com.ecommerce.user_service.service.EmployeeService;
import com.ecommerce.user_service.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final OAuth2Service oAuth2Service;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService,
            OAuth2Service oAuth2Service, EmployeeService employeeService, CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.oAuth2Service = oAuth2Service;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/customer/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of("token", jwt));
    }

    @PostMapping("/employee/login")
    public ResponseEntity<?> employeeLogin(@RequestBody Map<String,String> request) {
        String email = request.get("email");
        String password = request.get("password");
        System.out.println("email: " + email + " password: " + password + " received.");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        System.out.println("Authentication successful for email: " + email);
        final UserDetails userDetails = customUserDetailsService.loadEmployeeByUsername(email);
        System.out.println("UserDetails retrieved for email: " + email);
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        System.out.println("JWT generated for email: " + email);
        return ResponseEntity.ok(Map.of("token",jwt));
    }

    @GetMapping("/google/login")
    public ResponseEntity<?> googleLogin(@RequestParam("token") String idToken) {
        return ResponseEntity.ok(oAuth2Service.authenticateGoogleUser(idToken));
    }
}
