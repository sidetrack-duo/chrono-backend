package com.chrono.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    //현재 프로젝트 트래킹 중인지 여부
    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    //활설 비활성 여부 변화
    public void deactivate(){
        this.active = active;
    }

    public void activate(){
        this.active= true;
    }
}
