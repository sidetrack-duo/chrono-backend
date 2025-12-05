package com.chrono.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="RefreshToken")
public class RefreshTokenEntity {

    @Id
    private Long userId;

    @Column(nullable = false, length = 500)
    private String token;

    public void updateToken(String token){
        this.token = token;
    }
}
