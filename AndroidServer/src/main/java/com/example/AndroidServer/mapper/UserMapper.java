package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.Admin;
import com.example.AndroidServer.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE `u_name` = #{name} AND `u_password` = #{password}")
    User selectByName(@Param("name") String name,@Param("password") String password);
}