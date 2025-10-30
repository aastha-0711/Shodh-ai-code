package com.example.demo.repository;

import com.example.demo.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // ✅ Used by ContestController
    List<Submission> findByContest_Id(Long contestId);

    // ✅ Used by LeaderboardController and SubmissionController
    List<Submission> findByContest_IdOrderByCreatedAtDesc(Long contestId);

    // Compatibility adapter: some controllers call findByContestIdOrderByCreatedAtDesc
    // while Spring Data property path resolution here uses an underscore form
    // (findByContest_IdOrderByCreatedAtDesc). Provide a default delegating
    // method so both names work and controllers don't need edits.
    default List<Submission> findByContestIdOrderByCreatedAtDesc(Long contestId) {
        return findByContest_IdOrderByCreatedAtDesc(contestId);
    }
}
