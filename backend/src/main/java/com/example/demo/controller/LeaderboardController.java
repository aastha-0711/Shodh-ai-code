package com.example.demo.controller;

import com.example.demo.model.Submission;
import com.example.demo.repository.SubmissionRepository;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contests")
@CrossOrigin(origins = "*")
public class LeaderboardController {

    private final SubmissionRepository submissionRepository;

    public LeaderboardController(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @GetMapping("/{contestId}/leaderboard")
    public List<Map<String, Object>> getLeaderboard(@PathVariable Long contestId) {
        List<Submission> submissions = submissionRepository.findByContestIdOrderByCreatedAtDesc(contestId);

        // username → solved problems
        Map<String, Set<Long>> solved = new HashMap<>();
        for (Submission s : submissions) {
            if (!"Accepted".equalsIgnoreCase(s.getStatus())) continue;
            if (s.getUser() == null || s.getProblem() == null) continue;

            String username = s.getUser().getUsername();
            Long problemId = s.getProblem().getId();

            solved.computeIfAbsent(username, k -> new HashSet<>()).add(problemId);
        }

        // build leaderboard
        return solved.entrySet().stream()
                .map(e -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("username", e.getKey());
                    row.put("score", e.getValue().size());
                    return row;
                })
                .sorted((a, b) -> ((Integer) b.get("score")).compareTo((Integer) a.get("score")))
                .collect(Collectors.toList());
    }
}
