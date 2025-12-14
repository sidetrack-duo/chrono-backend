package com.chrono.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="EmailVerification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailVerificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="email_id", nullable = false)
    private long emailId;

    @Column(nullable = false)
    private String email;

    @Column(name="valid_code", nullable = false, length = 50)
    private String validCode;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean verified;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
