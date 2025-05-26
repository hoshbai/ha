package com.example.campus_life_assistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    // 新增界面组件
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

        initViews();
        setupRefreshLayout();
        checkAuthAndLoadData();
    }

    private void initViews() {
        // 初始化所有视图组件
        recyclerView = findViewById(R.id.rv_history);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tv_empty);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // 配置RecyclerView
        adapter = new BookAdapter(this, historyBooks);
        adapter.setOnItemClickListener(this); // 设置点击监听
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadHistoryData(false); // 不显示进度条
        });
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
        api.getHistory("Bearer " + getToken())
                .enqueue(new Callback<List<Book>>() {
                    @Override
                    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                        handleLoadingFinish();
                        if (response.isSuccessful()) {
                            handleSuccessResponse(response.body());
                        } else {
                            handleErrorResponse(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Book>> call, Throwable t) {
                        handleLoadingFinish();
                        showToast("网络异常: " + t.getMessage());
                    }
                });
    }

    private void handleSuccessResponse(List<Book> books) {
        historyBooks.clear();
        if (books != null && !books.isEmpty()) {
            historyBooks.addAll(books);
            tvEmpty.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("暂无阅读记录");
        }
        adapter.updateData(historyBooks);
    }

    private void handleErrorResponse(int statusCode) {
        String errorMsg = "加载失败，错误码：" + statusCode;
        if (statusCode == 401) {
            errorMsg = "登录已过期，请重新登录";
            clearUserSession();
        }
        tvEmpty.setText(errorMsg);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    private void handleLoadingFinish() {
        progressBar.setVisibility(View.GONE);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    // 实现书籍点击监听
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

    private void clearUserSession() {
        getSharedPreferences("user_session", MODE_PRIVATE).edit().clear().apply();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
