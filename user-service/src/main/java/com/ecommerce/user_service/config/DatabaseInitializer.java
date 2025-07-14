package com.ecommerce.user_service.config;

import com.ecommerce.user_service.model.Employee;
import com.ecommerce.user_service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
        return args -> {
            if (employeeRepository.count() == 0) {
                // Create admin user if no users exist
                Employee admin = new Employee();
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ROLE_ADMIN");
                admin.setDepartment("Administration");
                admin.setPosition("System Administrator");
                admin.setAddress("System Address");
                admin.setPhoneNumber("0000000000");
                admin.setDateOfJoining(java.time.LocalDate.now().toString());
                admin.setAbout("Initial system administrator");

                employeeRepository.save(admin);
                System.out.println("Created initial admin user: admin@example.com/admin123");
            }
        };
    }
}