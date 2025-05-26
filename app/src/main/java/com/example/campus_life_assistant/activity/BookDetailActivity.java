package com.example.campus_life_assistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sharedPref = getSharedPreferences("user_session", MODE_PRIVATE);
        book = getIntent().getParcelableExtra("book_data");
        ivFavorite = findViewById(R.id.iv_favorite);

        setupViews();
        setupFavoriteButton();
        recordViewHistory();
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
        price.setText("价格：" + book.getPrice() + "元");
        briefIntroduction.setText("内容简介：\n" + book.getBriefIntroduction());

        Glide.with(this)
                .load("http://10.0.2.2:8081/images/" + book.getImgUrl())
                .error(android.R.drawable.stat_notify_error)
                .into(imageView);
    }

    private void setupFavoriteButton() {
        updateFavoriteIcon();
        ivFavorite.setOnClickListener(v -> {
            if (!isUserLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(this, "需要登录后才能收藏", Toast.LENGTH_SHORT).show();
                return;
            }
            toggleFavorite();
        });
    }

    private void toggleFavorite() {
        String token = "Bearer " + sharedPref.getString("token", "");
        String action = book.isFavorite() ? "remove" : "add";

        ApiService api = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        api.toggleFavorite(book.getId(), action, token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    book.setFavorite(!book.isFavorite());
                    updateFavoriteIcon();
                } else {
                    Toast.makeText(BookDetailActivity.this, "操作失败，错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BookDetailActivity.this, "操作失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteIcon() {
        ivFavorite.setImageResource(book.isFavorite() ?
                R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
    }

    private void recordViewHistory() {
        if (!isUserLoggedIn()) return;
        String token = "Bearer " + sharedPref.getString("token", "");
        ApiService api = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        api.recordView(book.getId(), token).enqueue(new Callback<Void>() { // 修正参数顺序
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {}

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    private boolean isUserLoggedIn() {
        return sharedPref.contains("token");
    }
}
