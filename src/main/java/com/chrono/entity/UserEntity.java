package com.chrono.entity;

import com.chrono.enums.GithubConnectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name="Users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 60)
    private String email;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String githubUsername;

    @Column(nullable = true)
    private String githubAvatar;

    @Column
    private Long githubUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private GithubConnectStatus githubConnected = GithubConnectStatus.NONE;

    @Column(columnDefinition = "TEXT")
    private String githubPat;

    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public boolean isDeleted(){
        return deletedAt != null;
    }

    public void softDelete(){
        this.deletedAt = LocalDateTime.now();
    }

    public void restore(){
        this.deletedAt = null;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updatePassword(String encodedPassword){
        this.password = encodedPassword;
    }

    //깃허브연동 업데이트
    public void updateGithubBasic(String username, String avatarUrl, Long githubUserId) {
        this.githubUsername = username;
        this.githubAvatar = avatarUrl;
        this.githubUserId = githubUserId;
        this.githubConnected = GithubConnectStatus.BASIC;
    }

    //pat연동 업데이트
    public void updateGithubPat(String encryptedPat) {
        this.githubPat = encryptedPat;
        this.githubConnected = GithubConnectStatus.FULL;
    }
}
