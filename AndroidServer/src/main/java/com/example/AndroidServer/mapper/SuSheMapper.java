package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.Dormitory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SuSheMapper {
    @Update({
            "<script>",
            "UPDATE user u",
            "JOIN dormitory d ON d.building_no = #{buildingNo} AND d.room_no = #{roomNo}",
            "SET u.susheId = d.susheId",
            "WHERE u.u_name = #{username}",
            "</script>"
    })
    int updateDormitoryByBuildingAndRoom(@Param("buildingNo") String buildingNo,
                                         @Param("roomNo") String roomNo,
                                         @Param("username") String username);
}