package com.chrono.repository;

import com.chrono.entity.MessageEntity;
import com.chrono.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    Page<MessageEntity> findByReceiverAndDeletedByReceiverFalse(UserEntity receiver, Pageable pageable);

    Page<MessageEntity> findBySenderAndDeletedBySenderFalse(UserEntity sender, Pageable pageable);
}
