package com.student.app.users.service;

import com.student.app.users.model.User;
import com.student.app.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;

    }

    //signup logic
    public User registerUser(User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Email already in use");

        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


}
