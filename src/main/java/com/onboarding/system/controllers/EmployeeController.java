package com.onboarding.system.controllers;

import com.onboarding.system.models.Task;
import com.onboarding.system.models.User;
import com.onboarding.system.repositories.TaskRepository;
import com.onboarding.system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    // 1. მთავარი დაფის (Dashboard) გამოჩენა და პროგრესის გამოთვლა
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        List<Task> tasks = taskRepository.findByUser(currentUser);

        // პროგრესის დათვლა
        int totalTasks = tasks.size();

        long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        int progressPercentage = totalTasks == 0 ? 0 : (int) ((completedTasks * 100) / totalTasks);

        // XP ქულების დათვლა და Level-ის მინიჭება
        // შეცვალე ეს ხაზი:
        int totalXp = tasks.stream().filter(Task::isCompleted).mapToInt(Task::getXp).sum();
        String currentLevel = totalXp < 50 ? "Level 1: ახალბედა" :
                totalXp < 100 ? "Level 2: მკვლევარი" : "Level 3: ინტეგრირებული";

        // 🚀 ვამოწმებთ, არის თუ არა მიმდინარე იუზერი ვინმეს მენტორი
        List<User> mentees = userService.getMenteesByMentor(currentUser);
        boolean isMentor = !mentees.isEmpty();

        model.addAttribute("user", currentUser);
        model.addAttribute("tasks", tasks);
        model.addAttribute("progress", progressPercentage);
        model.addAttribute("xp", totalXp);
        model.addAttribute("level", currentLevel);
        model.addAttribute("isMentor", isMentor); // 🚀 გადავეცით თემპლეითს!
        model.addAttribute("feedback", new com.onboarding.system.models.Feedback());

        return "dashboard"; // დაარენდერებს dashboard.html-ს
    }

    // 2. დავალების შესრულების მონიშვნა (შეთანხმებულია dashboard.html-ის /tasks/complete/{id} მისამართთან)
    @PostMapping("/tasks/complete/{id}")
    public String completeTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setCompleted(!task.isCompleted()); // 🚀 იცვლება სტატუსი (toggle): მონიშვნა/მოხსნა მუშაობს ორმხრივად!
        taskRepository.save(task);
        return "redirect:/dashboard";
    }
}