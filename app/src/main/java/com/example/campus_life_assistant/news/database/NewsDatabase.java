package com.example.campus_life_assistant.news.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.campus_life_assistant.news.dao.CollectedNewsDao;
import com.example.campus_life_assistant.news.model.CollectedNews;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CollectedNews.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract CollectedNewsDao collectedNewsDao();

    private static volatile NewsDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static NewsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    NewsDatabase.class, "news_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
} 