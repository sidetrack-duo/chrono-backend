package com.chrono.entity;

import com.chrono.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Projects")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String owner; //깃 레포 오너

    @Column(nullable = false)
    private String repoName;

    @Column(nullable = false)
    private String repoUrl;

    @Column(unique = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate targetDate;

    @ElementCollection
    @CollectionTable(name="project_tech_stack", joinColumns = @JoinColumn(name="project_id"))
    @Column(name = "tech")
    private List<String> techStack;

    @Builder.Default
    private Integer totalCommits = 0;

    private LocalDateTime lastCommitAt;


    //현재 프로젝트 트래킹 중인지 여부
    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist(){
        this.status = ProjectStatus.IN_PROGRESS;
        if(this.totalCommits == null){
            this.totalCommits = 0;
        }
    }

    public void updateCommitStats(int totalCommits, LocalDateTime lastCommitAt) {
        this.totalCommits = totalCommits;
        this.lastCommitAt = lastCommitAt;
    }

    //활설 비활성 여부 변화
    public void deactivate(){
        this.active = false;
    }

    public void activate(){
        this.active= true;
    }

    public void markCompleted(){
        this.status = ProjectStatus.COMPLETED;
    }
}
