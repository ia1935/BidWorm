package com.student.app.bidworm.user.controller;


import com.student.app.bidworm.user.model.User;
import com.student.app.bidworm.jwt.JwtTokenProvider;
import com.student.app.bidworm.user.repository.UserRepository;
import com.student.app.bidworm.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<Map<String,String>> registerUser(@RequestBody User user) {
        Map<String,String> response = new HashMap<>();
        try{


            userService.registerUser(user);
            response.put("message","Registration complete! Check your email for a verification link" +
                    " to use Bidworm!");
            return ResponseEntity.ok().body(response);

        }catch (IllegalArgumentException e){
            response.put("message","Invalid email or email in use.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody User user) {
        Map<String,String> response = new HashMap<>();
        Optional<User> authenticatedUser = userService.loginUser(user.getEmail(), user.getPassword());

        if (authenticatedUser.isPresent()) {
            // Get the authenticated user
            User loggedInUser = authenticatedUser.get();

            // Generate the JWT token
            String token = jwtTokenProvider.generateToken(loggedInUser.getEmail());

            // Construct the response with token and user information
            response.put("uuid",loggedInUser.getUuid().toString());
            response.put("verification_status",loggedInUser.getVerified().toString());
            response.put("email",loggedInUser.getEmail());
            response.put("username",loggedInUser.getUsername());

            return ResponseEntity.ok(response);
        } else {
            // Return an error response for invalid credentials
            response.put("message","Unable to Login. Incorrect Username or Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        }
    }




    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);


    }


    //Email verification endpoint
    @GetMapping("/verify")
    public ResponseEntity<Map<String,String>> verifyEmail(@RequestParam("token") String token) {

        Map<String, String> response = new HashMap<>();


        //token validation
        System.out.println("Verifying email with token: " + token);
        if(!jwtTokenProvider.validateToken(token)) {

            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        //find user by token
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("Found user: " + user.getEmail());

            //handling expiration, sending new mail
            if (user.getVerificationExpiration().isBefore(LocalDateTime.now())){

                userService.refreshAndUpdateToken(user,token);
                response.put("message", "Invalid or expired token. Check your email for new token.");

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            if (!user.getVerified()) {
                user.setVerified(true);
                userRepository.save(user);
                System.out.println("User verified successfully.");

            }


            response.put("message", "Email successfully verified");
            return ResponseEntity.ok(response);
        }
        response.put("message", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }




}
