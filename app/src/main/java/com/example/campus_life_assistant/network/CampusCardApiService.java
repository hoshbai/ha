package com.example.campus_life_assistant.network;

import com.example.campus_life_assistant.campuscard.model.CampusCard;
import com.example.campus_life_assistant.campuscard.model.CardTransaction;
import com.example.campus_life_assistant.campuscard.model.InternetFee;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;

public interface CampusCardApiService {

    // 根据用户ID获取校园卡信息
    @GET("campuscard/user/{userId}")
    Call<CampusCard> getCampusCardByUserId(@Path("userId") String userId);

    // 根据卡ID获取校园卡信息
    @GET("campuscard/{cardId}")
    Call<CampusCard> getCampusCardByCardId(@Path("cardId") String cardId);

    // 创建新的校园卡
    @POST("campuscard")
    Call<Void> createCampusCard(@Body CampusCard campusCard);

    // 校园卡充值
    @POST("campuscard/recharge")
    Call<Void> recharge(@Query("cardId") String cardId, @Query("amount") BigDecimal amount);

    // 校园卡消费
    @POST("campuscard/consume")
    Call<String> consume(@Query("cardId") String cardId, @Query("amount") BigDecimal amount, @Query("description") String description);

    // 支付网费
    @POST("campuscard/payInternetFee")
    Call<Void> payInternetFee(@Query("cardId") String cardId, @Query("amount") BigDecimal amount);

    // 根据卡ID获取交易记录
    @GET("campuscard/{cardId}/transactions")
    Call<List<CardTransaction>> getTransactionsByCardId(@Path("cardId") String cardId);

    // 根据用户ID获取网费信息
    @GET("campuscard/internetFee/user/{userId}")
    Call<InternetFee> getInternetFeeByUserId(@Path("userId") String userId);

    // 挂失校园卡
    @POST("campuscard/{cardId}/reportLoss")
    Call<Void> reportLoss(@Path("cardId") String cardId);

    // 解挂校园卡
    @POST("campuscard/{cardId}/unreportLoss")
    Call<Void> unreportLoss(@Path("cardId") String cardId);

    // 可以根据需要添加其他API接口
} 