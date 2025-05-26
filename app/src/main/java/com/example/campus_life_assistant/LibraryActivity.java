package com.example.campus_life_assistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.campus_life_assistant.Adapter.BookAdapter;
import com.example.campus_life_assistant.Adapter.LibraryTabPagerAdapter;
import com.example.campus_life_assistant.activity.FavoritesActivity;
import com.example.campus_life_assistant.activity.HistoryActivity;
import com.example.campus_life_assistant.activity.SearchActivity;
import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.network.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibraryActivity extends AppCompatActivity {

    // 核心修复点：声明成员变量
    private List<Book> books = new ArrayList<>();
    private BookAdapter adapter;

    private static final String BASE_URL = "http://10.0.2.2:8081/api/";
    private final String[] titles = {
            "全部",
            "小说·文学",
            "科技·IT·互联网",
            "历史·传记",
            "哲学",
            "艺术·设计·摄影",
            "经济·金融",
            "科学·自然",
            "计算机",
            "医学·健康·养生"
    };

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private RecyclerView dailyRecView;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // 关键修复1：初始化适配器
        adapter = new BookAdapter(this, books);

        // 初始化各个视图组件
        setupToolbar();
        initFabSearch();
        initRetrofit();
        setupViewComponents();
        setupFavoriteListener();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("图书馆");
        }
    }

    private void initFabSearch() {
        FloatingActionButton fabSearch = findViewById(R.id.fabSearch);
        fabSearch.setOnClickListener(v ->
                startActivity(new Intent(LibraryActivity.this, SearchActivity.class))
        );
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }
    private void openFavorites() {
        // 检查登录状态
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        if (!prefs.contains("token")) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        // 跳转到收藏界面
        startActivity(new Intent(this, FavoritesActivity.class));
    }
    // 新增打开阅读历史功能
    private void openHistory() {
        // 检查登录状态
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        if (!prefs.contains("token")) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        startActivity(new Intent(this, HistoryActivity.class));
    }
    private void setupViewComponents() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        dailyRecView = findViewById(R.id.dailyRecommendationsRecyclerView);
        findViewById(R.id.btnWishlist).setOnClickListener(v -> openFavorites());
        findViewById(R.id.btnReadingHistory).setOnClickListener(v -> openHistory());

        // 设置横向推荐列表
        dailyRecView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dailyRecView.setAdapter(adapter);

        // 设置分类Tabs
        viewPager.setAdapter(new LibraryTabPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();

        fetchRecommendedBooks();
    }

    private void setupFavoriteListener() {
        // 关键修复2：使用成员变量适配器
        adapter.setOnFavoriteClickListener((book, position) -> {
            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            if (!prefs.contains("token")) {
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }

            String token = "Bearer " + prefs.getString("token", ""); // 获取token
            String action = book.isFavorite() ? "remove" : "add";
            ApiService api = ApiServiceHolder.getApiService();
            api.toggleFavorite(book.getId(), action, token).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // 关键修复3：使用成员变量books
                        books.set(position, book.updateFavoriteStatus());
                        adapter.notifyItemChanged(position);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("操作失败: " + t.getMessage());
                }
            });
        });
    }

    private void fetchRecommendedBooks() {
        apiService.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 关键修复4：更新成员变量
                    books.clear();
                    books.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (books.isEmpty()) {
                        showToast("无推荐图书");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                showToast("网络错误: " + t.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Retrofit单例封装
    private static class ApiServiceHolder {
        private static final ApiService INSTANCE = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        static ApiService getApiService() {
            return INSTANCE;
        }
    }
}
