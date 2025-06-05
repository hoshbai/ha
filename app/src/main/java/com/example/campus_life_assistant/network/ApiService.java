package com.example.campus_life_assistant.network;

import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.model.LibraryNotification;
import com.example.campus_life_assistant.model.ChargeHistory;
import com.example.campus_life_assistant.model.Dormitory;
import com.example.campus_life_assistant.model.ChargeRequest;
import com.example.campus_life_assistant.model.ChargeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // —— 新增：已登录用户获取包含收藏状态和阅读次数的书籍详情
    @GET("library/{bookId}")
    Call<Book> getBookDetailsWithFavorite(
            @Path("bookId") int bookId,
            @Header("Authorization") String authorization
    );

    // —— 未登录用户获取基础书籍详情
    @GET("library/{bookId}")
    Call<Book> getBookDetails(@Path("bookId") int bookId);

    // 阅读历史
    @GET("library/history")
    Call<List<Book>> getHistory(@Header("Authorization") String token);

    // 收藏列表
    @GET("library/favorites")
    Call<List<Book>> getFavorites(@Header("Authorization") String token);

    // 添加/取消收藏
    @POST("library/books/{bookId}/favorite")
    Call<Void> toggleFavorite(
            @Path("bookId") int bookId,
            @Query("action") String action,
            @Header("Authorization") String authorization
    );

    // 记录阅读（阅读次数累加）
    @POST("library/books/{bookId}/record-view")
    Call<Void> recordView(
            @Path("bookId") int bookId,
            @Header("Authorization") String authorization
    );

    // 关键字搜索
    @GET("library/search")
    Call<List<Book>> searchBooks(@Query("keyword") String keyword);

    // 获取所有图书
    @GET("library/books")
    Call<List<Book>> getAllBooks();

    // 按分类获取图书
    @GET("library/books")
    Call<List<Book>> getBooksByCategory(
            @Query(value = "category", encoded = true) String category
    );

    // 图书通知
    @GET("library/notifications")
    Call<List<LibraryNotification>> getNotifications();

    // 更新图书状态（管理功能）
    @POST("library/books/{id}/status")
    Call<Void> updateBookStatus(
            @Path("id") int id,
            @Query("status") String status
    );

    // 宿舍相关接口
    @GET("sushe/loadChargeHistory")
    Call<List<ChargeHistory>> getChargeHistory(
            @Query("buildingNo") String buildingNo,
            @Query("roomNo") String roomNo
    );

    @GET("sushe/getBalance")
    Call<Dormitory> getBalance(
            @Query("buildingNo") String buildingNo,
            @Query("roomNo") String roomNo
    );

    @GET("sushe/dormitoryInfo")
    Call<Dormitory> getDormitoryInfo(
            @Query("username") String username
    );

    @GET("sushe/getAllDormitories")
    Call<List<Dormitory>> getAllDormitories();

    @POST("sushe/updateDormitory")
    Call<Boolean> updateDormitory(@Body Dormitory dormitory);

    @POST("sushe/charge")
    Call<ChargeResponse> chargeElectricity(@Body ChargeRequest request);
}
