package com.example.campus_life_assistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.campus_life_assistant.Adapter.BookAdapter;
import com.example.campus_life_assistant.Adapter.LibraryTabPagerAdapter;
import com.example.campus_life_assistant.activity.SearchActivity;
import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.network.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibraryActivity extends AppCompatActivity {

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

        // 初始化Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("图书馆");
        }

        // 初始化FAB搜索按钮
        FloatingActionButton fabSearch = findViewById(R.id.fabSearch);
        fabSearch.setOnClickListener(v -> {
            Intent intent = new Intent(LibraryActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // 初始化视图
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        dailyRecView = findViewById(R.id.dailyRecommendationsRecyclerView);

        setupTabs();
        fetchRecommendedBooks();
    }

    private void setupTabs() {
        LibraryTabPagerAdapter adapter = new LibraryTabPagerAdapter(this);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();
    }

    private void fetchRecommendedBooks() {
        Call<List<Book>> call = apiService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();
                    if (books.isEmpty()) {
                        Toast.makeText(LibraryActivity.this, "无推荐图书", Toast.LENGTH_SHORT).show();
                    } else {
                        BookAdapter adapter = new BookAdapter(LibraryActivity.this, books);
                        dailyRecView.setLayoutManager(
                                new LinearLayoutManager(LibraryActivity.this,
                                        LinearLayoutManager.HORIZONTAL, false));
                        dailyRecView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(LibraryActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
