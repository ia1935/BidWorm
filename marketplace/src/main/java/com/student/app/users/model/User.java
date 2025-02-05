package com.student.app.users.model;

import jakarta.persistence.*;

import java.util.UUID;
@Entity
@Table(name = "users")
public class User {

        @Id
        @GeneratedValue
        @Column(name = "userid", updatable = false, nullable = false)
        private UUID userid;

        @Column(name = "name",nullable = false)
        private String name;
        @Column(name = "email",nullable = false,unique = true)
        private String email;

        @Column(name="password_hash",nullable = false)
        private String passwordHash;

        @Transient
        private String password;

        @Column(name="verification_token")
        private String verificationToken;

        @Column(name = "is_verified")
        private boolean isVerified;




    public User(String name, String email, String passwordHash) {
                this.name = name;
                this.email = email;
                this.passwordHash = passwordHash;
            }

        public User() {}


    public UUID getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPassword() {
        return password;
    }


    public void setUserid(UUID userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}