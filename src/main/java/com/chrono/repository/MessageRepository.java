package com.chrono.repository;

import com.chrono.dto.UserSearchResponseDto;
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

    @Query("""
        select new com.chrono.dto.UserSearchResponseDto(
            u.userId,
            u.nickname,
            u.email,
            u.githubUsername
        )
        from UserEntity u
        where u.deletedAt is null
          and u.userId <> :requesterId
          and (
            u.nickname like concat('%', :keyword, '%')
            or u.githubUsername like concat('%', :keyword, '%')
            or u.email like concat('%', :keyword, '%')
          )
    """)
    Page<UserSearchResponseDto> searchUsers(
            @Param("keyword") String keyword,
            @Param("requesterId") Long requesterId,
            Pageable pageable
    );

    long countByReceiverAndIsReadFalseAndDeletedByReceiverFalse(UserEntity receiver);


}
