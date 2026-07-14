package com.onboarding.system.controllers;

import com.onboarding.system.models.Feedback;
import com.onboarding.system.models.User;
import com.onboarding.system.repositories.FeedbackRepository;
import com.onboarding.system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/feedback/submit")
    public String submitFeedback(@RequestParam int moodScore,
                                 @RequestParam(required = false) String comment) {

        // 1. ვიღებთ მიმდინარე ავტორიზებულ იუზერს Spring Security-დან
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email);

        // 2. ვქმნით და ვინახავთ ფიდბექს
        Feedback feedback = new Feedback();
        feedback.setEmployee(user);
        feedback.setMoodScore(moodScore);
        feedback.setComment(comment);
        feedback.setSubmissionDate(LocalDate.now());

        feedbackRepository.save(feedback);

        return "redirect:/dashboard"; // ბრუნდება დაფაზე
    }
}