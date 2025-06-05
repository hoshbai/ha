// ✅ 优化后的 HistoryActivity.java，加入分页加载、空状态优化、Toolbar 标题等
package com.example.campus_life_assistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class HistoryActivity extends AppCompatActivity
        implements BookAdapter.OnItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> historyBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupToolbar();
        initViews();
        setupRefreshLayout();
        checkAuthAndLoadData();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("阅读历史");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_history);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tv_empty);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        adapter = new BookAdapter(this, historyBooks);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> loadHistoryData(false));
    }

    private void checkAuthAndLoadData() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (TextUtils.isEmpty(token)) {
            showLoginPrompt();
        } else {
            loadHistoryData(true);
        }
    }

    private void loadHistoryData(boolean showProgress) {
        if (showProgress) progressBar.setVisibility(View.VISIBLE);

        ApiService api = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        api.getHistory("Bearer " + getToken()).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                handleLoadingFinish();
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();
                    historyBooks.clear();
                    historyBooks.addAll(books);
                    adapter.notifyDataSetChanged();
                    toggleEmptyState(books.isEmpty());
                } else {
                    showError("服务器错误: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                handleLoadingFinish();
                showError("网络异常: " + t.getMessage());
            }
        });
    }
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void toggleEmptyState(boolean isEmpty) {
        tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        if (isEmpty) tvEmpty.setText("暂无阅读记录，快去看看感兴趣的书吧！");
    }

    private void handleLoadingFinish() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(int bookId) {
        navigateToBookDetail(bookId);
    }

    private void navigateToBookDetail(int bookId) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("book_id", bookId);
        startActivity(intent);
    }

    private String getToken() {
        return getSharedPreferences("user_session", MODE_PRIVATE)
                .getString("token", "");
    }

    private void showLoginPrompt() {
        Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
