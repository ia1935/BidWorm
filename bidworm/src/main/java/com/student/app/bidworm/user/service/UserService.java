package com.student.app.bidworm.user.service;


import com.student.app.bidworm.user.model.User;
import com.student.app.bidworm.jwt.JwtTokenProvider;
import com.student.app.bidworm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JavaMailSender mailSender;




    //Generating token JWT
    //going to make token provider class for this
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private EmailService emailService;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;


    }



    //signup logic
    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));

        // Generate a new token
        String token = jwtTokenProvider.generateToken(user.getEmail());

        // Set the token and expiration date
        user.setVerificationToken(token);
        user.setVerificationExpiration(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        // Sending the verification email
        emailService.sendVerificationEmail(user.getEmail(), token);
    }













    public Optional<User> loginUser(String email, String password) {
        // Fetch user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Validate password
            if (passwordEncoder.matches(password, user.getPasswordHash())) {

                // Check if user is not already verified
                if (!user.getVerified()) {
                    user.setVerified(true); // Set verified to true
                    userRepository.save(user); // Save the updated user to the database
                }

                // Return the authenticated user
                return Optional.of(user);
            }
        }

        // Return empty if login fails
        return Optional.empty();
    }




    public boolean verifyUser(String token) {
        // Find user by verification token
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the token has expired
            if (user.getVerificationExpiration() != null && user.getVerificationExpiration().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Verification token has expired");
            }

            // Mark user as verified
            user.setVerified(true);
            user.setVerificationToken(null); // Clear token after verification
            user.setVerificationExpiration(null); // Clear expiration date

            userRepository.save(user);
            return true; // Verification successful
        }
        return false; // Invalid token
    }


    //refreshing our token, going to send a new email
    public void refreshAndUpdateToken(User user,String oldToken) {
        String newToken = jwtTokenProvider.refreshToken(oldToken);  // Generate new token
        userRepository.updateVerificationToken(oldToken, newToken);  // Update the database with the new token

        emailService.sendVerificationEmail(user.getEmail(), newToken);
    }
}

