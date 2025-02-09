package com.student.app.bidworm.user.service;


import com.student.app.bidworm.user.model.User;
import com.student.app.bidworm.user.provider.JwtTokenProvider;
import com.student.app.bidworm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;


    }



    //signup logic
    public User registerUser(User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Email already in use");

        }

        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));

        //Making verification token
        String token = jwtTokenProvider.getEmailFromToken(user.getEmail());

        //token and exp date
        user.setVerificationToken(token);
        user.setVerificationExpiration(LocalDateTime.now().plusHours(1));


        userRepository.save(user);

        //sending verification email:
        sendVerificationEmail(user.getEmail(), token);

        return user;
    }



    //sending the email function

    private void sendVerificationEmail(String email, String token) {
        //content
        //replace message link with domain in production
        String subject = "Please verify your email account at Bidworm";
        String message = "To verify your email, click the following link:\n"+
                "http://localhost:8080/users/verify?token="+token;


        //sending mail:
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);

    }







    public Optional<User> loginUser(String email, String password) {
        // Fetch user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Validate password
            if (passwordEncoder.matches(password, user.getPasswordHash())) {

                // Check if user is not already verified
                if (!user.isVerified()) {
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
}

