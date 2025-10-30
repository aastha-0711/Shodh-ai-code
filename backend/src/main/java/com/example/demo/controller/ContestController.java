package com.example.demo.controller;

import com.example.demo.model.Contest;
import com.example.demo.model.Submission;
import com.example.demo.repository.ContestRepository;
import com.example.demo.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contests")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContestController {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @GetMapping
    public List<Map<String, Object>> getAllContests() {
        List<Contest> contests = contestRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Contest contest : contests) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", contest.getId());
            data.put("name", contest.getName());
            data.put("description", contest.getDescription());
            result.add(data);
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContestById(@PathVariable Long id) {
        return contestRepository.findById(id)
                .map(contest -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", contest.getId());
                    response.put("name", contest.getName());
                    response.put("description", contest.getDescription());

                    // Map problems to simple DTO-like maps to avoid serializing full entity graph
                    List<Map<String, Object>> problems = contest.getProblems().stream().map(p -> {
                        Map<String, Object> pm = new HashMap<>();
                        pm.put("id", p.getId());
                        pm.put("title", p.getTitle());
                        pm.put("description", p.getDescription());
                        return pm;
                    }).collect(Collectors.toList());

                    response.put("problems", problems);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ FIXED LEADERBOARD ENDPOINT
    @GetMapping("/{id}/leaderboard")
    public ResponseEntity<?> getLeaderboard(@PathVariable Long id) {
        Optional<Contest> contestOpt = contestRepository.findById(id);
        if (contestOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Contest not found"));
        }

        List<Submission> submissions = submissionRepository.findByContest_Id(id);

        // Group by username and count only correct submissions
        Map<String, Long> leaderboard = submissions.stream()
                .filter(Submission::isCorrect)
                .collect(Collectors.groupingBy(
                        s -> s.getUser().getUsername(),
                        Collectors.counting()
                ));

        // Sort descending
        List<Map<String, Object>> sorted = leaderboard.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", entry.getKey());
                    map.put("solvedCount", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(sorted);
    }
}
