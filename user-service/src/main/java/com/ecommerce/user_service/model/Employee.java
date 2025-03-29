package com.ecommerce.user_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String role; // "ADMIN" or other roles
    private String department;
    private String position;
    // Add other employee-specific fields
}