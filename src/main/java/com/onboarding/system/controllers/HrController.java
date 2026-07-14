package com.onboarding.system.controllers;

import com.onboarding.system.models.Feedback;
import com.onboarding.system.models.User;
import com.onboarding.system.repositories.FeedbackRepository;
import com.onboarding.system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HrController {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    // 1. HR Dashboard
    @GetMapping("/hr/dashboard")
    public String hrDashboard(Model model) {
        List<User> employees = userService.getAllUsers();
        List<Feedback> feedbacks = feedbackRepository.findAllByOrderBySubmissionDateDesc();

        model.addAttribute("employees", employees);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("allUsers", employees);

        return "hr-dashboard";
    }

    // 2. მომხმარებლის დამატება
    @GetMapping("/hr/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    @PostMapping("/hr/users/add")
    public String addUser(@ModelAttribute User user) {
        // 🚀 ვიყენებთ UserService-ს, რომელიც თავად მიხედავს დეფოლტ პაროლს და დავალებების შექმნას
        userService.saveUser(user);
        return "redirect:/hr/dashboard";
    }

    // 3. მენტორის მიბმა
    // განახლებული მეთოდი ორმხრივი მენტორობის ბლოკირებით
    @PostMapping("/hr/users/{employeeId}/assign-mentor")
    public String assignMentor(@PathVariable Long employeeId, @RequestParam(required = false) Long mentorId) {
        User employee = userService.findById(employeeId);

        // 🚀 თუ HR-მა აირჩია "მენტორის მოხსნა" (ID არის 0), ვუშლით მენტორს
        if (mentorId == null || mentorId == 0) {
            employee.setMentor(null);
            userService.saveUser(employee);
            return "redirect:/hr/dashboard";
        }

        User mentor = userService.findById(mentorId);

        // ბექენდ დაცვა ორმხრივი ციკლისგან
        if (mentor.getMentor() != null && mentor.getMentor().getId().equals(employeeId)) {
            return "redirect:/hr/dashboard?error=circular_mentor";
        }

        employee.setMentor(mentor);
        userService.saveUser(employee);

        return "redirect:/hr/dashboard";
    }

    // 4. რედაქტირების გვერდი
    @GetMapping("/hr/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "edit-user";
    }

    // 5. რედაქტირების შენახვა
    @PostMapping("/hr/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User userDetails) {
        User existingUser = userService.findById(id);

        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setRole(userDetails.getRole());
        existingUser.setEnabled(userDetails.isEnabled());

        userService.saveUser(existingUser);
        return "redirect:/hr/dashboard";
    }

    // 6. მომხმარებლის წაშლა (ზუსტად ემთხვევა შენს მისამართს: /hr/users/1/delete)
    @GetMapping("/hr/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/hr/dashboard";
    }
}