package com.chrono.repository;

import com.chrono.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmailAndDeletedAtIsNull(String email);

    boolean existsByNicknameAndDeletedAtIsNull(String nickname);

    Optional<UserEntity> findByEmailAndDeletedAtIsNull(String email);

}
