package com.onboarding.system.services;

import com.onboarding.system.models.User;
import com.onboarding.system.models.Task;
import com.onboarding.system.repositories.UserRepository;
import com.onboarding.system.repositories.TaskRepository;
import com.onboarding.system.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 🚀 განახლებული შენახვა: ახალ მომხმარებელს ავტომატურად უქმნის გლობალურ მისიებს
    // 🚀 განახლებული saveUser მეთოდი დეფოლტ პაროლით და მისიებით:
    public User saveUser(User user) {
        boolean isNew = (user.getId() == null);

        if (isNew && (user.getPassword() == null || user.getPassword().isEmpty())) {
            user.setPassword("123456");
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        } else {
            // ყოველი შემთხვევისთვის, როლი ყოველთვის დიდი ასოებით შევინახოთ
            user.setRole(user.getRole().toUpperCase());
        }

        User savedUser = userRepository.save(user);

        // 🚀 უსაფრთხო შემოწმება: დიდი ასოებით იქნება თუ პატარა, მისიები მაინც შეიქმნება!
        if (isNew && "USER".equalsIgnoreCase(savedUser.getRole())) {
            createDefaultTasks(savedUser);
        }
        return savedUser;
    }

    // 🚀 გლობალური დეფოლტ დავალებების გენერატორი (შინაგანაწესი, უსაფრთხოება)
    // 🚀 განახლებული createDefaultTasks მეთოდი სწორი სეტერით:
    // 🚀 განახლებული createDefaultTasks მეთოდი სწორი სეტერით:
    private void createDefaultTasks(User user) {
        Task t1 = new Task();
        t1.setTitle("🏢 შინაგანაწესის გაცნობა");
        t1.setDescription("წაიკითხე კომპანიის შინაგანაწესი და სამუშაო ეთიკის დოკუმენტი.");
        t1.setXp(20); // 🚀 შეიცვალა setXp-ზე!
        t1.setCompleted(false);
        t1.setUser(user);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setTitle("🛡️ საინფორმაციო უსაფრთხოება");
        t2.setDescription("გაეცანი კიბერუსაფრთხოების ძირითად წესებს და გაიარე ტესტირება.");
        t2.setXp(20); // 🚀 შეიცვალა setXp-ზე!
        t2.setCompleted(false);
        t2.setUser(user);
        taskRepository.save(t2);

        Task t3 = new Task();
        t3.setTitle("🤝 პირველი შეხვედრა მენტორთან");
        t3.setDescription("დაუკავშირდი შენს მენტორს და დაგეგმე გაცნობითი ონლაინ შეხვედრა.");
        t3.setXp(20); // 🚀 შეიცვალა setXp-ზე!
        t3.setCompleted(false);
        t3.setUser(user);
        taskRepository.save(t3);
    }

    public List<User> getMenteesByMentor(User mentor) {
        if (mentor == null || mentor.getId() == null) {
            return List.of();
        }
        return userRepository.findByMentorId(mentor.getId());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("მომხმარებელი არ მოიძებნა: " + email));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("მომხმარებელი არ მოიძებნა ID-ით: " + id));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        taskRepository.deleteAll(taskRepository.findByUser(user));
        feedbackRepository.deleteAll(feedbackRepository.findByEmployee(user));

        List<User> mentees = userRepository.findAll().stream()
                .filter(u -> u.getMentor() != null && u.getMentor().getId().equals(id))
                .toList();
        for (User mentee : mentees) {
            mentee.setMentor(null);
            userRepository.save(mentee);
        }
        userRepository.delete(user);
    }
}