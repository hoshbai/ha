package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.ChargeHistory;
import com.example.AndroidServer.model.Dormitory;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("SELECT susheId AS id, building_no AS buildingNo, room_no AS roomNo, balance FROM dormitory")
    List<Dormitory> findAll();

    @Select("SELECT susheId AS id, building_no AS buildingNo, room_no AS roomNo, balance FROM dormitory WHERE susheId = #{id}")
    Dormitory findById(@Param("id") Integer id);

    @Select("SELECT susheId AS id, building_no AS buildingNo, room_no AS roomNo, balance FROM dormitory WHERE building_no = #{buildingNo} AND room_no = #{roomNo}")
    Dormitory findByBuildingAndRoom(@Param("buildingNo") String buildingNo,
                                    @Param("roomNo") String roomNo);

    @Select("SELECT id, dormitory_id AS dormitoryId, amount, name, charge_time AS date FROM charge_history WHERE dormitory_id = #{dormitoryId}")
    List<ChargeHistory> findByDormitoryId(@Param("dormitoryId") Long dormitoryId);


    @Update("UPDATE dormitory SET balance = #{balance} WHERE susheId = #{id}")
    void updateBalance(Dormitory dormitory);

    @Insert({
            "INSERT INTO charge_history (dormitory_id, amount, name)",
            "VALUES (#{dormitoryId}, #{amount}, #{name})"
    })
    void insertInToChargeHistory(ChargeHistory history);
}