package com.onboarding.system.repositories;

import com.onboarding.system.models.Task;
import com.onboarding.system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // ეს მეთოდი გვეხმარება, რომ კონკრეტული იუზერის დავალებები ვიპოვოთ ბაზაში
    List<Task> findByUser(User user);
}