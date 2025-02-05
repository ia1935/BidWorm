package com.student.app.users.controller;


import com.student.app.users.model.User;
import com.student.app.users.repository.UserRepository;
import com.student.app.users.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }


    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        Optional<User> authenticatedUser = userService.loginUser(user.getEmail(), user.getPassword());
        return authenticatedUser.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);


    }


    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        Optional<User> userOptional = userRepository.findbyVerificationToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);

            return ResponseEntity.ok("Email Verified Successfully!");

        }
        return ResponseEntity.badRequest().body("Invalid Token.");


    }
}