package com.example.campus_life_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.Adapter.FoodCommentAdapter;
import com.example.campus_life_assistant.entry.Food;
import com.example.campus_life_assistant.entry.FoodComment;
import com.example.campus_life_assistant.manager.CanteenManager;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView foodImage;
    private TextView tvFoodName;
    private TextView tvFoodDescription;
    private TextView tvFoodPrice;
    private TextView tvFoodCategory;
    private RatingBar rbFoodRating;
    private TextView tvRatingCount;
    private EditText etComment;
    private RatingBar rbUserRating;
    private Button btnSubmitComment;
    private RecyclerView rvComments;

    private Food food;
    private CanteenManager canteenManager;
    private FoodCommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // 获取传递的食品对象
        food = (Food) getIntent().getSerializableExtra("food");
        if (food == null) {
            Toast.makeText(this, "加载菜品信息失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 初始化视图
        initViews();

        // 初始化数据
        canteenManager = CanteenManager.getInstance(this);

        // 显示食品信息
        displayFoodInfo();

        // 设置评论RecyclerView
        setupCommentsRecyclerView();

        // 设置评论提交按钮
        setupSubmitButton();
    }

    private void initViews() {
        foodImage = findViewById(R.id.food_image);
        tvFoodName = findViewById(R.id.tv_food_name);
        tvFoodDescription = findViewById(R.id.tv_food_description);
        tvFoodPrice = findViewById(R.id.tv_food_price);
        tvFoodCategory = findViewById(R.id.tv_food_category);
        rbFoodRating = findViewById(R.id.rb_food_rating);
        tvRatingCount = findViewById(R.id.tv_rating_count);
        etComment = findViewById(R.id.et_comment);
        rbUserRating = findViewById(R.id.rb_user_rating);
        btnSubmitComment = findViewById(R.id.btn_submit_comment);
        rvComments = findViewById(R.id.rv_comments);
    }

    private void displayFoodInfo() {
        // 设置标题
        setTitle(food.getName());

        // 显示食品详情
        tvFoodName.setText(food.getName());
        tvFoodDescription.setText(food.getDescription());
        tvFoodPrice.setText(String.format("¥%.2f", food.getPrice()));
        tvFoodCategory.setText(food.getCategory());
        rbFoodRating.setRating(food.getRating());

        // 显示评分计数
        if (food.getRatingCount() > 0) {
            tvRatingCount.setText(String.format("(%d人评价)", food.getRatingCount()));
        } else {
            tvRatingCount.setText("(暂无评价)");
        }

        // 设置图片（此处应使用图片加载库如Glide或Picasso）
        // Glide.with(this).load(food.getImageUrl()).into(foodImage);

        // 临时使用占位图
        foodImage.setImageResource(R.drawable.food_placeholder);
    }

    private void setupCommentsRecyclerView() {
        // 设置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(layoutManager);

        // 创建并设置适配器
        commentAdapter = new FoodCommentAdapter(this, food.getComments());
        rvComments.setAdapter(commentAdapter);
    }

    private void setupSubmitButton() {
        btnSubmitComment.setOnClickListener(v -> {
            float rating = rbUserRating.getRating();
            String comment = etComment.getText().toString().trim();

            // 验证评分
            if (rating == 0) {
                Toast.makeText(this, "请先给菜品评分", Toast.LENGTH_SHORT).show();
                return;
            }

            // 提交评分和评论
            canteenManager.addRatingAndComment(food, rating, comment, "匿名用户");

            // 更新UI
            rbFoodRating.setRating(food.getRating());
            tvRatingCount.setText(String.format("(%d人评价)", food.getRatingCount()));

            // 刷新评论列表
            commentAdapter.updateData(food.getComments());

            // 清空输入
            etComment.setText("");
            rbUserRating.setRating(0);

            Toast.makeText(this, "评分提交成功", Toast.LENGTH_SHORT).show();
        });
    }
}