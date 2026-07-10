package com.onboarding.system.services;

import com.onboarding.system.models.User;
import com.onboarding.system.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // from base all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // new employee
    public User saveUser(User user) {
        user.setRole("USER");
        return userRepository.save(user);
    }
}