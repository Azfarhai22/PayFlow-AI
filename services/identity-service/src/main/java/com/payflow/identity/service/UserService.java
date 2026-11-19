package com.payflow.identity.service;

import org.springframework.stereotype.Service;
import com.payflow.identity.entity.User;
import com.payflow.identity.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service 
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(String fullName, String email, String password) {
        if (isEmailExists(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(fullName, email, encodedPassword);
        return userRepository.save(user);
        
    

    }

}
