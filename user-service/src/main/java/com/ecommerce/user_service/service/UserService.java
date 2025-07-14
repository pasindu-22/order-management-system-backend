package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.UserRegistrationDTO;
import com.ecommerce.user_service.exception.UserAlreadyExistsException;
import com.ecommerce.user_service.model.User;
import com.ecommerce.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        log.info("Creating new user with email: {}", user.getEmail());
        return userRepository.save(user);
    }

    public User registerUser(UserRegistrationDTO registrationDTO) {
        // Check if user already exists
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + registrationDTO.getEmail() + " already exists");
        }

        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(registrationDTO.getRole());

        log.info("Registering new user with email: {}", user.getEmail());
        return userRepository.save(user);
    }

    public User updateUser(String id, User userUpdates) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(userUpdates.getName());
            user.setAddresses(userUpdates.getAddresses());
            // Don't update email or password here - use separate methods
            log.info("Updating user with ID: {}", id);
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(String id) {
        log.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }
}
