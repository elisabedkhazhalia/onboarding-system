package com.onboarding.system.controllers;

import com.onboarding.system.models.Task;
import com.onboarding.system.models.User;
import com.onboarding.system.repositories.TaskRepository;
import com.onboarding.system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MentorController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    // 1. მენტორის მთავარი პანელი
    @GetMapping("/mentor/dashboard")
    public String mentorDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User mentor = userService.findByEmail(auth.getName());

        // ვიღებთ ამ მენტორის ყველა სტუდენტს
        List<User> mentees = userService.getMenteesByMentor(mentor);

        model.addAttribute("mentor", mentor);
        model.addAttribute("mentees", mentees);
        return "mentor-dashboard";
    }

    // 2. კონკრეტული სტუდენტის დავალებების მართვა
    @GetMapping("/mentor/mentees/{id}/tasks")
    public String manageMenteeTasks(@PathVariable Long id, Model model) {
        User mentee = userService.findById(id);
        List<Task> tasks = taskRepository.findByUser(mentee);

        // ვიანგარიშებთ მიმდინარე პროგრესს
        long completedCount = tasks.stream().filter(Task::isCompleted).count();
        int progress = tasks.isEmpty() ? 0 : (int) ((completedCount * 100) / tasks.size());

        model.addAttribute("mentee", mentee);
        model.addAttribute("tasks", tasks);
        model.addAttribute("progress", progress);
        model.addAttribute("newTask", new Task());
        return "mentor-tasks";
    }

    // 3. მენტორის მიერ ახალი ტექნიკური დავალების დამატება
    @PostMapping("/mentor/mentees/{menteeId}/tasks/add")
    public String addTask(@PathVariable Long menteeId, @ModelAttribute("newTask") Task task) {
        User mentee = userService.findById(menteeId);
        task.setUser(mentee);
        task.setCompleted(false);
        taskRepository.save(task);
        return "redirect:/mentor/mentees/" + menteeId + "/tasks";
    }

    // 4. დავალების წაშლა
    @GetMapping("/mentor/tasks/{taskId}/delete")
    public String deleteTask(@PathVariable Long taskId, @RequestParam Long menteeId) {
        taskRepository.deleteById(taskId);
        return "redirect:/mentor/mentees/" + menteeId + "/tasks";
    }
}