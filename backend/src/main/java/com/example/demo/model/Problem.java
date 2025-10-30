package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<TestCase> testCases;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    // Default constructor
    public Problem() {}

    // Constructor used when manually setting id + test cases
    public Problem(Long id, String title, String description, List<TestCase> testCases) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.testCases = testCases;
    }

    // Simple constructor (no contest link)
    public Problem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // ✅ Constructor used by DataLoader
    public Problem(String title, String description, Contest contest) {
        this.title = title;
        this.description = description;
        this.contest = contest;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<TestCase> getTestCases() { return testCases; }
    public void setTestCases(List<TestCase> testCases) { this.testCases = testCases; }

    public Contest getContest() { return contest; }
    public void setContest(Contest contest) { this.contest = contest; }
}
