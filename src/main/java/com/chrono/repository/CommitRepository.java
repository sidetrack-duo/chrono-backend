package com.chrono.repository;

import com.chrono.entity.CommitEntity;
import com.chrono.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<CommitEntity, Long> {

    boolean existsByProject_ProjectIdAndSha(Long projectId, String sha);

    List<CommitEntity> findByProject_ProjectIdOrderByCommitDateDesc(Long projectId);

    List<CommitEntity> findByProject(ProjectEntity project);

    List<CommitEntity> findByProject_ProjectIdAndCommitDateBetween(Long projectId, LocalDateTime start, LocalDateTime end);

}
