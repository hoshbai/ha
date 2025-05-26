package com.example.campus_life_assistant.network;

import com.example.campus_life_assistant.model.BasicResponse;
import com.example.campus_life_assistant.model.ChargeHistory;
import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.model.ChargeRequest;
import com.example.campus_life_assistant.model.ChargeResponse;
import com.example.campus_life_assistant.model.Dormitory;
import com.example.campus_life_assistant.model.LibraryNotification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("library/history")
    Call<List<Book>> getHistory(@Header("Authorization") String token);

    @GET("library/favorites")
    Call<List<Book>> getFavorites(@Header("Authorization") String token);

    @POST("library/books/{bookId}/favorite")
    Call<Void> toggleFavorite(
            @Path("bookId") int bookId,
            @Query("action") String action,
            @Header("Authorization") String authorization // 强制携带认证头
    );
    @POST("library/books/{bookId}/record-view")
    Call<Void> recordView(
            @Path("bookId") int bookId,
            @Header("Authorization") String authorization // 历史记录接口同样需要
    );

    // ✅ 修正前："api/library/search" → 修正后："library/search"
    // 最终URL = BASE_URL + "library/search" → /api/library/search
    @GET("library/search")
    Call<List<Book>> searchBooks(@Query("keyword") String keyword);

    // 保持原有
    @GET("library/books")
    Call<List<Book>> getAllBooks();

    // 保持原有
    @GET("library/{bookId}")
    Call<Book> getBookDetails(@Path("bookId") int bookId);

    @GET("library/books")
    Call<List<Book>> getBooksByCategory(
            @Query(value = "category", encoded = true) String category
    );

    @GET("library/notifications")
    Call<List<LibraryNotification>> getNotifications();

    // ✅ 修正前："library/books/{id}/status" → 保持原本正确
    @POST("library/books/{id}/status")
    Call<Void> updateBookStatus(@Path("id") int id, @Query("status") String status);

    //宿舍部分
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
