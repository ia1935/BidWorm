package com.student.app.bidworm.user.repository;

import com.student.app.bidworm.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository
        extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    @Modifying
    @Query("Update User u SET u.verificationToken" +
            " = :newToken WHERE u.verificationToken = " +
            ":oldToken")
    void updateVerificationToken(@Param("oldToken") String oldToken,
                                 @Param("newToken") String newToken);
}