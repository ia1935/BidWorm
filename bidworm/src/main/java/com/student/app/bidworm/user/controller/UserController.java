package com.student.app.bidworm.user.controller;


import com.student.app.bidworm.user.model.User;
import com.student.app.bidworm.user.provider.JwtTokenProvider;
import com.student.app.bidworm.user.repository.UserRepository;
import com.student.app.bidworm.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.student.app.bidworm.user.model.LoginResponse;

import java.time.LocalDateTime;
import java.util.Optional;


@RestController
@RequestMapping("/users/v1")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try{
            userService.registerUser(user);
            return ResponseEntity.ok("Registration Complete! To use Bidworm, " +
                    "check your email for a verification link!");

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody User user) {
        Optional<User> authenticatedUser = userService.loginUser(user.getEmail(), user.getPassword());

        if (authenticatedUser.isPresent()) {
            // Get the authenticated user
            User loggedInUser = authenticatedUser.get();

            // Generate the JWT token
            String token = jwtTokenProvider.generateToken(loggedInUser.getEmail());

            // Construct the response with token and user information
            LoginResponse response = new LoginResponse(
                    token,
                    loggedInUser.getEmail(),
                    loggedInUser.getUsername(),
                    loggedInUser.getUuid() // Assuming these fields exist in your User class
            );

            return ResponseEntity.ok(response);
        } else {
            // Return an error response for invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new LoginResponse("Invalid credentials",null,null,null)
            );
        }
    }




    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);


    }


    //Email verification endpoint
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        //token validation
        if(!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
        }

        //find user by token
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getVerificationExpiration().isBefore(LocalDateTime.now())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
            }

            user.setVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);

            return ResponseEntity.ok("Email Successfully verified");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");


    }

    @GetMapping
    String test(){
        return("Testing");
    }




}
