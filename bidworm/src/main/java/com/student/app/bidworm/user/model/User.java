package com.student.app.bidworm.user.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(unique = true,nullable = false)
    private String username;


    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name="password_hash",nullable = false)
    private String passwordHash;

    @Transient
    private String password;


    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "verified")
    private boolean verified = false;

    @Column(name = "verification_expiration")
    private LocalDateTime verificationExpiration;

    public User() {
    }

    public User(String email, String username, String password, String name) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }

}