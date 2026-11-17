package com.payflow.identity.service;

import org.springframework.stereotype.Service;

import com.payflow.identity.repository.UserRepository;

@Service 
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }       
    
}
