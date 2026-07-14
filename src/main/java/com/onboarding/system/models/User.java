package com.onboarding.system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    private boolean isEnabled = true;

    // 🚀 გადავიყვანეთ EAGER-ზე მყისიერი ჩატვირთვისთვის
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id")
    private User mentor;

    // 🚀 ჭკვიანი მეთოდი ორმხრივი მენტორობის და საკუთარი თავის მენტორობის დასაბლოკად
    public boolean canBeMentorFor(User student) {
        if (this.id.equals(student.getId())) {
            return false; // საკუთარი თავის მენტორი ვერ იქნება
        }
        // თუ მე (ამ კანდიდატს) უკვე მყავს მენტორად ეს სტუდენტი, მაშინ მე მისი მენტორი ვეღარ ვიქნები
        if (this.mentor != null && this.mentor.getId().equals(student.getId())) {
            return false;
        }
        return true;
    }
}