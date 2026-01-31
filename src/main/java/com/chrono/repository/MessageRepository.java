package com.chrono.repository;

import com.chrono.entity.MessageEntity;
import com.chrono.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    Page<MessageEntity> findByReceiverAndDeletedByReceiverFalse(UserEntity receiver, Pageable pageable);

    Page<MessageEntity> findBySenderAndDeletedBySenderFalse(UserEntity sender, Pageable pageable);

    @Query("""
        select m from MessageEntity m
        join fetch m.sender
        join fetch m.receiver
        where m.messageId = :messageId
    """)
    Optional<MessageEntity> findDetailById(@Param("messageId") Long messageId);

}
