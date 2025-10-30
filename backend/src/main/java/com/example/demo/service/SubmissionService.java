package com.example.demo.service;

import com.example.demo.model.Submission;
import com.example.demo.repository.SubmissionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.List;

@Service
public class SubmissionService {
    private final SubmissionRepository repo;
    private final String JUDGE_IMAGE = "shodhacode/judge:latest";
    private final long RUN_TIMEOUT_SECONDS = 5;
    private final String MEMORY_LIMIT = "256m";

    public SubmissionService(SubmissionRepository r) {
        this.repo = r;
    }

    public Submission submit(Submission s) {
        s.setStatus("Running");
        s = repo.save(s);
        processAsync(s.getId()); // fire and forget
        return s;
    }

    @Async
    public CompletableFuture<Void> processAsync(Long id) {
        new Thread(() -> run(id)).start();
        return CompletableFuture.completedFuture(null);
    }

    private void run(Long submissionId) {
        Submission s = repo.findById(submissionId).orElse(null);
        if (s == null) return;
        Path ws = null;
        try {
            ws = Files.createTempDirectory("judge-");
            Path main = ws.resolve("Main.java");
            Files.writeString(main, s.getCode(), StandardCharsets.UTF_8);

            // Compile inside docker judge image
            List<String> compileCmd = List.of(
                "docker", "run", "--rm",
                "-v", ws.toAbsolutePath().toString() + ":/workspace",
                "--network", "none",
                "--memory", MEMORY_LIMIT,
                JUDGE_IMAGE,
                "bash", "-lc",
                "cd /workspace && javac Main.java 2>&1"
            );

            ProcessBuilder pc = new ProcessBuilder(compileCmd);
            pc.redirectErrorStream(true);
            Process pC = pc.start();
            ByteArrayOutputStream cob = new ByteArrayOutputStream();
            try (InputStream is = pC.getInputStream()) { is.transferTo(cob); }
            boolean compiled = pC.waitFor(10, TimeUnit.SECONDS) && pC.exitValue() == 0;
            String compOut = cob.toString(StandardCharsets.UTF_8);
            if (!compiled) {
                s.setStatus("Error");
                s.setResultDetails("Compilation failed:\n" + compOut);
                repo.save(s);
                cleanup(ws);
                return;
            }

            // ✅ Simplified: just run once with no test cases
            List<String> runCmd = List.of(
                "docker", "run", "--rm",
                "-v", ws.toAbsolutePath().toString() + ":/workspace",
                "--network", "none",
                "--memory", MEMORY_LIMIT,
                JUDGE_IMAGE,
                "bash", "-lc",
                "cd /workspace && java Main"
            );

            ProcessBuilder pr = new ProcessBuilder(runCmd);
            pr.redirectErrorStream(true);
            Process pRun = pr.start();

            boolean finished = pRun.waitFor(RUN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                pRun.destroyForcibly();
                s.setStatus("Error");
                s.setResultDetails("Time limit exceeded");
                repo.save(s);
                cleanup(ws);
                return;
            }

            String out = new String(pRun.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            int exit = pRun.exitValue();

            s.setStatus("Accepted");
            s.setResultDetails("Program executed successfully.\nOutput:\n" + out + "\nExit Code: " + exit);
            repo.save(s);
            cleanup(ws);

        } catch (Exception e) {
            if (s != null) {
                s.setStatus("Error");
                s.setResultDetails(e.toString());
                repo.save(s);
            }
            cleanup(ws);
        }
    }

    private void cleanup(Path ws) {
        try {
            if (ws != null)
                Files.walk(ws)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> p.toFile().delete());
        } catch (Exception ignored) {}
    }
}
