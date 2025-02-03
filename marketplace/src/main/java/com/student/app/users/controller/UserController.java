package com.student.app.users.controller;


import com.student.app.users.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    private final UserService userService;



}
