package com.example.campus_life_assistant.news.api;

import com.example.campus_life_assistant.news.model.NewsItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("news")
    Call<List<NewsItem>> getNewsList();

    @GET("news/search")
    Call<List<NewsItem>> searchNews(@Query("keyword") String keyword);
} 