package com.onboarding.system.controllers;


import com.onboarding.system.models.Task;
import com.onboarding.system.repositories.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EmployeeController {

    private final TaskRepository taskRepository;

    public EmployeeController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/employee/dashboard")
    public String showEmployeeDashboard(Model model) {
        List<Task> tasks = taskRepository.findAll();

        long completedTasks = tasks.stream().filter(t -> "DONE".equals(t.getStatus())).count();
        int totalTasks = tasks.size();

        int progress = totalTasks > 0 ? (int) ((completedTasks * 100) / totalTasks) : 0;

        model.addAttribute("tasks", tasks);
        model.addAttribute("progress", progress);

        return "employee-dashboard";
    }
}