package com.chrono.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id", nullable = false)
    private ProjectEntity project;

    @Column(nullable = false, updatable = true)
    private String sha; //커밋 고유코드

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private String authorName;

    private String authorEmail;

    @Column(nullable = false)
    private LocalDateTime commitDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime savedAt;

}
