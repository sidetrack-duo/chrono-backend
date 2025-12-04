package com.chrono.entity;

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
}
