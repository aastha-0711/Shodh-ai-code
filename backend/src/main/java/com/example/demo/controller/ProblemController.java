package com.example.demo.controller;

import com.example.demo.model.Problem;
import com.example.demo.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProblemController {

    @Autowired
    private ProblemRepository problemRepository;

    // ✅ Get all problems
    @GetMapping
    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    // ✅ Get a single problem by ID
    @GetMapping("/{id}")
    public Problem getProblemById(@PathVariable Long id) {
        return problemRepository.findById(id).orElse(null);
    }
}
