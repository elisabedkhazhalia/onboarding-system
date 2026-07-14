package com.onboarding.system.repositories;

import com.onboarding.system.models.Feedback;
import com.onboarding.system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // 🚀 ჩაამატე ეს ხაზი და იუზერის წაშლისას ერორი გაქრება:
    List<Feedback> findByEmployee(User employee);

    List<Feedback> findByEmployeeOrderBySubmissionDateDesc(User employee);
    List<Feedback> findAllByOrderBySubmissionDateDesc();
}