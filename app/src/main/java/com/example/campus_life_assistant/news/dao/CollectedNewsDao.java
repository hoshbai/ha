package com.example.campus_life_assistant.news.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.campus_life_assistant.news.model.CollectedNews;

import java.util.List;

@Dao
public interface CollectedNewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCollectedNews(CollectedNews news);

    @Delete
    void deleteCollectedNews(CollectedNews news);

    @Query("SELECT * FROM collected_news")
    List<CollectedNews> getAllCollectedNews();

    @Query("SELECT EXISTS(SELECT 1 FROM collected_news WHERE newsId = :newsId LIMIT 1)")
    boolean isNewsCollected(String newsId);

    @Query("DELETE FROM collected_news WHERE newsId = :newsId")
    int deleteCollectedNewsById(String newsId);
} 