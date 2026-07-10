package com.onboarding.system.repositories;

import com.onboarding.system.models.Department;
import com.onboarding.system.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


}