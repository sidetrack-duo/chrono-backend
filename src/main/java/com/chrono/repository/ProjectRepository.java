package com.chrono.repository;

import com.chrono.entity.ProjectEntity;
import com.chrono.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByUser_UserId(Long userId);


    List<ProjectEntity> findAllByUserAndActiveTrue(UserEntity user);

    //활설 플젝 중복 체크
    boolean existsByUserAndOwnerAndRepoNameAndActiveTrue(
            UserEntity user, String owner, String repoName
    );

    //삭제 플젝 조회
    Optional<ProjectEntity> findByUserAndOwnerAndRepoNameAndActiveFalse(
            UserEntity user, String owner, String repoName
    );
}
