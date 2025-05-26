package com.example.campus_life_assistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.Adapter.BookAdapter;
import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.network.ApiService;
import com.example.campus_life_assistant.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> favoriteBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initViews();
        checkAuthAndLoadData();
        setupAdapterClick();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookAdapter(this, favoriteBooks);
        recyclerView.setAdapter(adapter);

        // 设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("我的收藏");
        }
    }

    private void checkAuthAndLoadData() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadFavoriteBooks(token);
    }

    private void loadFavoriteBooks(String token) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Book>> call = apiService.getFavorites(token); // 添加token参数
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoriteBooks.clear();
                    favoriteBooks.addAll(markFavorites(response.body()));
                    adapter.updateData(favoriteBooks);
                } else {
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(FavoritesActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Book> markFavorites(List<Book> books) {
        // 确保所有返回的书籍标记为收藏状态
        for (Book book : books) {
            book.setFavorite(true);
        }
        return books;
    }

    private void setupAdapterClick() {
        adapter.setOnItemClickListener(bookId -> {
            // 点击跳转到书籍详情（复用原有逻辑）
            navigateToBookDetail(bookId);
        });

        adapter.setOnFavoriteClickListener((book, position) -> {
            // 处理取消收藏操作
            handleUnfavorite(book, position);
        });
    }

    private void navigateToBookDetail(int bookId) {
        // 这里需要根据你的实现获取完整的Book对象（可能需要调用接口）
        // 此处简化为直接传递ID
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("book_id", bookId);
        startActivity(intent);
    }

    private void handleUnfavorite(Book book, int position) {
        String token = "Bearer " + getSharedPreferences("user_session", MODE_PRIVATE).getString("token", "");

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.toggleFavorite(book.getId(), "remove", token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // 本地删除并刷新
                            favoriteBooks.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(FavoritesActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(FavoritesActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEmptyState() {
        Toast.makeText(this, "您还没有收藏任何书籍", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
