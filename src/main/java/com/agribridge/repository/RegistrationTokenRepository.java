package com.agribridge.repository;

import com.agribridge.model.RegistrationVerificationToken;
import com.agribridge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationVerificationToken, Long>{
    Optional<RegistrationVerificationToken> findByToken(String token);
    Boolean existsByUser(User user);
    Optional<RegistrationVerificationToken> findByUser(User user);
}
