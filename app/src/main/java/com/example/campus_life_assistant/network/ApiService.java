// ApiService.java
package com.example.campus_life_assistant.network;

import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.model.LibraryNotification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ✅ 正确拼接路径：BASE_URL的 /api/ + "library/books" → /api/library/books
    @GET("library/books")
    Call<List<Book>> getAllBooks();

    // ✅ 正确拼接路径：BASE_URL的 /api/ + "library/{bookId}" → /api/library/{bookId}
    @GET("library/{bookId}")
    Call<Book> getBookDetails(@Path("bookId") int bookId);

    @GET("library/books")
    Call<List<Book>> getBooksByCategory(
            @Query(value = "category", encoded = true) String category
    );

    @GET("library/notifications")
    Call<List<LibraryNotification>> getNotifications();

    @POST("library/books/{id}/status")
    Call<Void> updateBookStatus(@Path("id") int id, @Query("status") String status);
}
