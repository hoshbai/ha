// ✅ 优化后的 FavoritesActivity.java，统一样式、支持空状态提示和Toolbar标题
package com.example.campus_life_assistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campus_life_assistant.LoginActivity;
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
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        setupToolbar();
        initViews();
        checkAuthAndLoadData();
        setupAdapterClick();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("我的收藏");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_favorites);
        tvEmpty = findViewById(R.id.tv_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookAdapter(this, favoriteBooks);
        recyclerView.setAdapter(adapter);
    }

    private void checkAuthAndLoadData() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        loadFavoriteBooks(token);
    }

    private void loadFavoriteBooks(String token) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Book>> call = apiService.getFavorites("Bearer " + token);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoriteBooks.clear();
                    favoriteBooks.addAll(markFavorites(response.body()));
                    adapter.notifyDataSetChanged();
                    toggleEmptyState(favoriteBooks.isEmpty());
                } else {
                    showError("服务器错误: " + response.code());
                    toggleEmptyState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                showError("网络错误: " + t.getMessage());
                toggleEmptyState(true);
            }
        });
    }

    private List<Book> markFavorites(List<Book> books) {
        for (Book book : books) {
            book.setFavorite(true);
        }
        return books;
    }

    private void setupAdapterClick() {
        adapter.setOnItemClickListener(bookId -> navigateToBookDetail(bookId));
        adapter.setOnFavoriteClickListener((book, position) -> handleUnfavorite(book, position));
    }

    private void navigateToBookDetail(int bookId) {
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
                            favoriteBooks.remove(position);
                            adapter.notifyItemRemoved(position);
                            toggleEmptyState(favoriteBooks.isEmpty());
                            Toast.makeText(FavoritesActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(FavoritesActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void toggleEmptyState(boolean isEmpty) {
        tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        if (isEmpty) tvEmpty.setText("暂无收藏书籍，快去添加一些感兴趣的吧！");
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
