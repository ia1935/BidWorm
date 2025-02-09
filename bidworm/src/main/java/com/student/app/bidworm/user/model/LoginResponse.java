package com.student.app.bidworm.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private String username;
    private UUID uuid;

}