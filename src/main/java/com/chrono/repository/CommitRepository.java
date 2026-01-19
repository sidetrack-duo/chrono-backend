package com.chrono.repository;

import com.chrono.entity.CommitEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<CommitEntity, Long> {

    //커밋 수 가져오기
    int countByProject_ProjectId(Long projectId);

    @Query("select c.sha from CommitEntity c where c.project.projectId = :projectId")
    List<String> findAllShasByProjectId(@Param("projectId") Long projectId);

}
