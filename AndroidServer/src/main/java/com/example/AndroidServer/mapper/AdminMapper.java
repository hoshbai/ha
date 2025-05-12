package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("SELECT * FROM admin WHERE a_name = #{name}")
    Admin selectByName(@Param("name") String name);
}