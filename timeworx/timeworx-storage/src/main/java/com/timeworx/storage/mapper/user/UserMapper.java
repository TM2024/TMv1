package com.timeworx.storage.mapper.user;

import com.timeworx.common.entity.user.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/15 8:59 PM
 */
public interface UserMapper {

    @Select("select `Id`, `Name`, `Password`, `Email`, `PhoneNo`, `Type`, `Status`, `Introduction`, `Avatar` from User where Id = #{userId} ")
    User findUserById(@Param("userId") Long userId);

    @Select("select `Id`, `Name`, `Password`, `Email`, `PhoneNo`, `Type`, `Status`, `Introduction`, `Avatar` from User where Email = #{email} ")
    User findUserByEmail(@Param("email") String email);
}
