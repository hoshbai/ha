package com.example.campus_life_assistant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.campus_life_assistant.LoginActivity;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.network.ApiService;
import com.example.campus_life_assistant.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    private Book book;
    private SharedPreferences sharedPref;
    private ImageView ivFavorite;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sharedPref = getSharedPreferences("user_session", MODE_PRIVATE);

        // 设置 Toolbar
        setupToolbar();

        // 获取书籍ID
        bookId = getIntent().getIntExtra("book_id", -1);
        book = getIntent().getParcelableExtra("book_data");

        if (bookId == -1 && book != null) {
            bookId = book.getId();
        }

        if (bookId == -1) {
            Toast.makeText(this, "书籍信息错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ivFavorite = findViewById(R.id.iv_favorite);

        // 加载完整的书籍信息（包括收藏状态）
        loadBookDetails();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("书籍详情");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void loadBookDetails() {
        SharedPreferences sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        ApiService api = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Book> call;
        if (!TextUtils.isEmpty(token)) {
            // 已登录用户，获取包含收藏状态的详情
            call = api.getBookDetailsWithFavorite(bookId, "Bearer " + token);
        } else {
            call = api.getBookDetails(bookId); // 未登录用户
        }
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    book = response.body(); // 更新本地 Book 对象
                    setupViews();
                    setupFavoriteButton(); // 更新收藏按钮状态
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });
    }

    private void setupViews() {
        TextView title = findViewById(R.id.title);
        TextView author = findViewById(R.id.author);
        ImageView imageView = findViewById(R.id.bookImageView);
        TextView publishingHouse = findViewById(R.id.publishingHouse);
        TextView publishDate = findViewById(R.id.publishDate);
        TextView isbn = findViewById(R.id.isbn);
        TextView price = findViewById(R.id.price);
        TextView briefIntroduction = findViewById(R.id.briefIntroduction);

        title.setText(book.getBookName());
        author.setText("作者：" + book.getAuthor());
        publishingHouse.setText("出版社：" + book.getPublishingHouse());
        publishDate.setText("出版日期：" + book.getPublishDate());
        isbn.setText("ISBN：" + book.getIsbn());
        price.setText("价格：￥" + book.getPrice());
        briefIntroduction.setText("内容简介：\n" + (book.getBriefIntroduction() != null ? book.getBriefIntroduction() : "暂无简介"));

        Glide.with(this)
                .load("http://10.0.2.2:8081/images/" + book.getImgUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(imageView);
    }

    // 确保在获取详情后更新收藏状态
    private void setupFavoriteButton() {
        updateFavoriteIcon(); // 根据 book.isFavorite() 更新 UI
        ivFavorite.setOnClickListener(v -> {
            if (!isUserLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(this, "需要登录后才能收藏", Toast.LENGTH_SHORT).show();
                return;
            }
            // 直接根据当前状态切换，无需依赖 UI 显示
            toggleFavorite();
        });
    }

    private void toggleFavorite() {
        String token = "Bearer " + sharedPref.getString("token", "");
        String action = book.isFavorite() ? "remove" : "add"; // 直接取当前状态
        ApiService api = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        api.toggleFavorite(book.getId(), action, token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    book.setFavorite(!book.isFavorite()); // 更新状态
                    updateFavoriteIcon();
                    Toast.makeText(BookDetailActivity.this,
                            book.isFavorite() ? "已收藏" : "已取消收藏",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void updateFavoriteIcon() {
        if (book != null) {
            ivFavorite.setImageResource(book.isFavorite() ?
                    R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        }
    }

    private void recordViewHistory() {
        if (!isUserLoggedIn()) return;
        String token = "Bearer " + sharedPref.getString("token", "");
        ApiService api = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        api.recordView(book.getId(), token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // 静默记录，不需要提示
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 静默失败，不影响用户体验
            }
        });
    }

    private boolean isUserLoggedIn() {
        return sharedPref.contains("token") && !TextUtils.isEmpty(sharedPref.getString("token", ""));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}