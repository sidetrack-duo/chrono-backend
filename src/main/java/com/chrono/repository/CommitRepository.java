package com.chrono.repository;

import com.chrono.entity.CommitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<CommitEntity, Long> {
    boolean existsByShaAndProject_ProjectId(String sha, Long projectId);

    List<CommitEntity> findByProject_ProjectId(Long projectId);
}
