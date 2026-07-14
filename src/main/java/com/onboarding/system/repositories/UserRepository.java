package com.onboarding.system.repositories;

import com.onboarding.system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // 🚀 ახალი სწრაფი და უსაფრთხო მეთოდი მენტორის ID-ით საძიებლად
    List<User> findByMentorId(Long mentorId);
}