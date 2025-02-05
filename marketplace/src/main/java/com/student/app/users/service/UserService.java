package com.student.app.users.service;

import com.student.app.users.model.User;
import com.student.app.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);

        User savedUser = userRepository.save(user);
        sendVerificationEmail(savedUser);

        return savedUser;
    }

    private void sendVerificationEmail(User user){
        String verificationLink = "https://yourdomain.me/users/verify?token="
                + user.getVerificationToken();
        String subject = "Verify your email";
        String body = "Hello " + user.getName() +
                ",\n\nPlease verify your email by clicking the link below:\n"
                + verificationLink;

        emailService.sendEmail(user.getEmail(), subject, body);

    }







    public Optional<User> loginUser(String email, String password){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPasswordHash())){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
