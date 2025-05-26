package com.example.campus_life_assistant.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.Adapter.BookAdapter;
import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.network.ApiService;
import com.example.campus_life_assistant.network.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private BookAdapter adapter;
    private List<Book> bookList = new ArrayList<>();
    private TextView tvEmpty;
    private RecyclerView rvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextInputEditText etSearch = findViewById(R.id.etSearch);
        rvResults = findViewById(R.id.rvResults);
        tvEmpty = findViewById(R.id.tvEmpty);

        // 初始化RecyclerView
        adapter = new BookAdapter(this, bookList);
        rvResults.setLayoutManager(new LinearLayoutManager(this));
        rvResults.setAdapter(adapter);

        // 设置键盘搜索键监听
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.getText().toString().trim());
                return true;
            }
            return false;
        });

        // 设置文本变化监听（带防抖）
        etSearch.addTextChangedListener(new TextWatcher() {
            private Runnable searchRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) {
                    etSearch.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> {
                    String query = s.toString().trim();
                    if (!TextUtils.isEmpty(query) && query.length() >= 2) {
                        performSearch(query);
                    }
                };
                etSearch.postDelayed(searchRunnable, 500); // 500ms防抖
            }
        });
    }

    private void performSearch(String keyword) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.searchBooks(keyword).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> results = response.body();
                    updateUI(results);
                } else {
                    showError("服务器错误: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                showError("网络错误: " + t.getMessage());
            }
        });
    }

    private void updateUI(List<Book> results) {
        runOnUiThread(() -> {
            if (results == null || results.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvResults.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvResults.setVisibility(View.VISIBLE);
                bookList.clear();
                bookList.addAll(results);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
