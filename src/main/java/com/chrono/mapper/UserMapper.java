package com.chrono.mapper;

import com.chrono.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserEntity selectById(@Param("userId") Long userId);

    int updateNickname(@Param("userId") Long userId, @Param("nickname") String nickname);
}
