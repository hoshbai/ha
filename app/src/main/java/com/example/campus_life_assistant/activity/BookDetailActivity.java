package com.example.campus_life_assistant.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.model.Book;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Book book = getIntent().getParcelableExtra("book_data");
        if (book != null) {
            // 绑定所有视图
            TextView title = findViewById(R.id.title);
            TextView author = findViewById(R.id.author);
            ImageView imageView = findViewById(R.id.bookImageView);
            TextView publishingHouse = findViewById(R.id.publishingHouse);
            TextView publishDate = findViewById(R.id.publishDate);
            TextView isbn = findViewById(R.id.isbn);
            TextView price = findViewById(R.id.price);
            TextView briefIntroduction = findViewById(R.id.briefIntroduction);

            // 设置内容
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
    }
}
