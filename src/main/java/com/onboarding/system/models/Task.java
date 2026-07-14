package com.onboarding.system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // დავალების სახელი (მაგ. "გაეცანი შინაგანაწესს")
    private String description;

    private boolean isCompleted; // შესრულებულია თუ არა

    private int xp; // რამდენი ქულა ენიჭება ამ დავალების შესრულებას

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // რომელ თანამშრომელს ეკუთვნის დავალება
}