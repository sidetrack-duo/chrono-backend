package com.chrono.mapper;

import com.chrono.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserEntity selectById(@Param("userId") Long userId);
}
