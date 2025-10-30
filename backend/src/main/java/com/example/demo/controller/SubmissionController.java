package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "*")
public class SubmissionController {

    private final SubmissionRepository submissionRepository;
    private final ContestRepository contestRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    public SubmissionController(SubmissionRepository submissionRepository,
                                ContestRepository contestRepository,
                                ProblemRepository problemRepository,
                                UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.contestRepository = contestRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }

    // ✅ CREATE submission
    @PostMapping
    public ResponseEntity<?> createSubmission(@RequestBody Map<String, Object> payload) {
        try {
            Long problemId = payload.get("problemId") != null
                    ? ((Number) payload.get("problemId")).longValue() : null;
            String username = (String) payload.get("username");
            String code = (String) payload.get("code");
            String language = (String) payload.getOrDefault("language", "java");
            Long contestId = payload.containsKey("contestId")
                    ? ((Number) payload.get("contestId")).longValue() : 1L;

            // ✅ Validation
            if (problemId == null || username == null || code == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
            }

            Optional<Contest> contestOpt = contestRepository.findById(contestId);
            Optional<Problem> problemOpt = problemRepository.findById(problemId);
            if (contestOpt.isEmpty() || problemOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid contest or problem ID"));
            }

            // ✅ Get or create user
            User user = userRepository.findByUsername(username).orElseGet(() -> {
                User u = new User();
                u.setUsername(username);
                return userRepository.save(u);
            });

            // ✅ Simulate judging
            String status;
            String resultDetails;
            if (code.trim().isEmpty()) {
                status = "Compilation Error";
                resultDetails = "No code provided.";
            } else if (!code.contains("System.out.println")) {
                status = "Runtime Error";
                resultDetails = "Program did not produce output.";
            } else {
                status = "Accepted";
                resultDetails = "Program executed successfully.";
            }

            // ✅ Save submission
            Submission sub = new Submission();
            sub.setContest(contestOpt.get());
            sub.setProblem(problemOpt.get());
            sub.setUser(user);
            sub.setCode(code);
            sub.setLanguage(language);
            sub.setStatus(status);
            sub.setResultDetails(resultDetails);
            submissionRepository.save(sub);

            return ResponseEntity.ok(Map.of(
                    "submissionId", sub.getId(),
                    "status", sub.getStatus(),
                    "resultDetails", sub.getResultDetails()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get all submissions for a specific contest (used by leaderboard)
    @GetMapping("/{contestId}")
    public ResponseEntity<?> getSubmissionsByContest(@PathVariable Long contestId) {
        try {
            List<Submission> submissions = submissionRepository.findByContestIdOrderByCreatedAtDesc(contestId);

            List<Map<String, Object>> response = new ArrayList<>();
            for (Submission s : submissions) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", s.getId());
                map.put("status", s.getStatus());
                map.put("language", s.getLanguage());
                map.put("createdAt", s.getCreatedAt());
                map.put("updatedAt", s.getUpdatedAt());
                if (s.getUser() != null) map.put("username", s.getUser().getUsername());
                if (s.getProblem() != null) map.put("problemId", s.getProblem().getId());
                response.add(map);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get single submission by ID (fixes 500 error for /api/submissions/:id)
    @GetMapping("/single/{id}")
    public ResponseEntity<?> getSubmissionById(@PathVariable Long id) {
        try {
            Optional<Submission> subOpt = submissionRepository.findById(id);
            if (subOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Submission not found"));
            }

            Submission sub = subOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", sub.getId());
            response.put("status", sub.getStatus());
            response.put("resultDetails", sub.getResultDetails());
            response.put("language", sub.getLanguage());
            response.put("createdAt", sub.getCreatedAt());
            response.put("updatedAt", sub.getUpdatedAt());
            if (sub.getProblem() != null)
                response.put("problemId", sub.getProblem().getId());
            if (sub.getUser() != null)
                response.put("username", sub.getUser().getUsername());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
