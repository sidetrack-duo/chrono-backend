package com.chrono.repository;

import com.chrono.entity.ProjectEntity;
import com.chrono.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByUser_UserId(Long userId);

    boolean existsByUser_UserIdAndOwnerAndRepoName(Long userId, String owner, String repoName);

    List<ProjectEntity> findAllByUserAndActiveTrue(UserEntity user);

}
