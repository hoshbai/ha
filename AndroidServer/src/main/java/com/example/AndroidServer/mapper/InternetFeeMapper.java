package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.InternetFee;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface InternetFeeMapper {

    // 根据用户ID获取网费信息
    @Select("SELECT * FROM internet_fee WHERE user_id = #{userId}")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "balance", column = "balance"),
        @Result(property = "lastUpdateTime", column = "last_update_time")
    })
    InternetFee getInternetFeeByUserId(@Param("userId") String userId);

    // 更新网费余额
    @Update("UPDATE internet_fee SET balance = #{balance}, last_update_time = #{lastUpdateTime} WHERE user_id = #{userId}")
    void updateInternetFee(InternetFee internetFee);

    // 插入新的网费记录 (用于用户第一次使用网费功能)
    @Insert("INSERT INTO internet_fee (user_id, balance, last_update_time) VALUES (#{userId}, #{balance}, #{lastUpdateTime})")
    void insertInternetFee(InternetFee internetFee);
} 