package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.CampusCard;
import com.example.AndroidServer.model.CardTransaction;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CampusCardMapper {

    @Select("SELECT card_id, user_id, balance, create_time, update_time, status FROM campus_card WHERE user_id = #{userId}")
    @Results(id = "campusCardResultMap", value = {
            @Result(property = "cardId", column = "card_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "balance", column = "balance"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "status", column = "status")
    })
    CampusCard getCampusCardByUserId(String userId);

    @Select("SELECT card_id, user_id, balance, create_time, update_time, status FROM campus_card WHERE card_id = #{cardId}")
    @ResultMap("campusCardResultMap")
    CampusCard getCampusCardByCardId(String cardId);

    @Insert("INSERT INTO campus_card (card_id, user_id, balance, create_time, update_time, status) VALUES (#{cardId}, #{userId}, #{balance}, #{createTime}, #{updateTime}, #{status})")
    void insertCampusCard(CampusCard campusCard);

    @Update("UPDATE campus_card SET balance = #{balance}, update_time = #{updateTime} WHERE card_id = #{cardId}")
    void updateCampusCard(CampusCard campusCard);

    @Insert("INSERT INTO card_transaction (card_id, amount, transaction_type, transaction_time, description) VALUES (#{cardId}, #{amount}, #{transactionType}, #{transactionTime}, #{description})")
    void insertCardTransaction(CardTransaction transaction);

    @Select("SELECT transaction_id, card_id, amount, transaction_type, transaction_time, description FROM card_transaction WHERE card_id = #{cardId} ORDER BY transaction_time DESC")
    @Results(id = "cardTransactionResultMap", value = {
            @Result(property = "transactionId", column = "transaction_id"),
            @Result(property = "cardId", column = "card_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "transactionType", column = "transaction_type"),
            @Result(property = "transactionTime", column = "transaction_time"),
            @Result(property = "description", column = "description")
    })
    List<CardTransaction> getTransactionsByCardId(String cardId);

    @Update("UPDATE campus_card SET status = #{status} WHERE card_id = #{cardId}")
    void updateCampusCardStatus(@Param("cardId") String cardId, @Param("status") int status);
    
    // 可以根据需要添加其他方法，比如根据卡ID查询校园卡信息等
} 