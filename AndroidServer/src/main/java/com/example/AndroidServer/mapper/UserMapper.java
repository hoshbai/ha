package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.Admin;
import com.example.AndroidServer.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE `u_name` = #{name} AND `u_password` = #{password}")
    User selectByName(@Param("name") String name,@Param("password") String password);

    @Insert("INSERT INTO user (u_name, u_password) VALUES (#{name}, #{password})")
    void insertByRegister(@Param("name") String name, @Param("password") String password);
    @Select("SELECT * FROM user WHERE u_name = #{name}")
    User selectByNameOnly(@Param("name") String name);
    @Select("SELECT COUNT(*) FROM user WHERE u_name = #{name}")
    int countByName(@Param("name") String name);
}