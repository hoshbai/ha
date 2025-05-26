package com.example.campus_life_assistant.news.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.news.model.NewsItem;
import com.example.campus_life_assistant.news.model.NewsComment;
import com.example.campus_life_assistant.news.adapter.NewsCommentAdapter;
import com.example.campus_life_assistant.news.database.NewsDatabase;
import com.example.campus_life_assistant.news.dao.CollectedNewsDao;
import com.example.campus_life_assistant.news.model.CollectedNews;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.bumptech.glide.Glide;

public class NewsDetailActivity extends AppCompatActivity {

    private RecyclerView rvComments;
    private NewsCommentAdapter commentAdapter;
    private List<NewsComment> commentList = new ArrayList<>();
    private EditText etComment;
    private Button btnSendComment;
    private CollectedNewsDao collectedNewsDao;
    private NewsItem currentNewsItem;
    private ImageView ivCollect;
    private ImageView ivLike;
    private boolean isNewsLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("资讯详情");

        // Get data from intent
        NewsItem newsItem = (NewsItem) getIntent().getSerializableExtra("newsItem");
        currentNewsItem = newsItem;
        isNewsLiked = getIntent().getBooleanExtra("isLiked", false);

        if (newsItem != null) {
            ImageView ivNewsImage = findViewById(R.id.iv_news_detail_image);
            TextView tvNewsTitle = findViewById(R.id.tv_news_detail_title);
            TextView tvNewsPublisherTime = findViewById(R.id.tv_news_detail_publisher_time);
            TextView tvNewsContent = findViewById(R.id.tv_news_detail_content);
            TextView tvViewsLikes = findViewById(R.id.tv_news_detail_views_likes);

            // Set data to views
            tvNewsTitle.setText(newsItem.getTitle());
            tvNewsPublisherTime.setText("发布者：" + newsItem.getPublisher() + " 发布时间: " + newsItem.getPublishTime());

            // 直接从 newsItem 对象获取新闻内容
            tvNewsContent.setText(newsItem.getContent());

            tvViewsLikes.setText("浏览: " + newsItem.getViews() + " 点赞: " + newsItem.getLikes());

            // Load image using Glide (from local resources)
            int imageResource = getResources().getIdentifier(
                newsItem.getImageUrl(), "drawable", getPackageName());

            if (imageResource != 0) {
                Glide.with(this)
                     .load(imageResource)
                     .placeholder(R.drawable.ic_launcher_background) // Optional: Placeholder image while loading
                     .error(R.drawable.ic_launcher_background) // Optional: Image to show if loading fails
                     .into(ivNewsImage);
            } else {
                // Handle case where resource is not found, e.g., set a default image
                ivNewsImage.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        // 获取底部操作图标
        ivCollect = findViewById(R.id.iv_news_detail_collect);
        ivLike = findViewById(R.id.iv_news_detail_like);
        ImageView ivShare = findViewById(R.id.iv_news_detail_share);

        // Set initial like icon based on isNewsLiked variable
        if (isNewsLiked) {
            ivLike.setImageResource(R.drawable.ic_favorite_filled_red);
        }

        // 获取 CollectedNewsDao 实例
        collectedNewsDao = NewsDatabase.getDatabase(this).collectedNewsDao();

        // 检查新闻是否已被收藏并更新图标状态
        checkIfCollected();

        // 设置点击事件监听器
        ivCollect.setOnClickListener(v -> {
            if (currentNewsItem != null) {
                NewsDatabase.databaseWriteExecutor.execute(() -> {
                    boolean isCollected = collectedNewsDao.isNewsCollected(currentNewsItem.getTitle()); // Using title as a simple unique identifier for now
                    if (isCollected) {
                        // Already collected, uncollect
                        collectedNewsDao.deleteCollectedNewsById(currentNewsItem.getTitle());
                        runOnUiThread(() -> {
                            ivCollect.setImageResource(R.drawable.ic_star_border); // Change to outline star icon
                            Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // Not collected, collect
                        CollectedNews newsToCollect = new CollectedNews(
                            currentNewsItem.getTitle(), // Using title as newsId for now
                            currentNewsItem.getTitle(),
                            currentNewsItem.getPublisher(),
                            currentNewsItem.getPublishTime(),
                            currentNewsItem.getImageUrl()
                        );
                        collectedNewsDao.insertCollectedNews(newsToCollect);
                        runOnUiThread(() -> {
                            ivCollect.setImageResource(R.drawable.ic_star_filled_yellow); // Change to filled yellow star icon
                            Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        });

        ivLike.setOnClickListener(v -> {
            if (currentNewsItem != null) {
                int currentLikes = currentNewsItem.getLikes();
                TextView tvViewsLikes = findViewById(R.id.tv_news_detail_views_likes);

                if (isNewsLiked) {
                    // Already liked, unlike
                    currentNewsItem.setLikes(currentLikes - 1); // Decrement likes
                    isNewsLiked = false; // Update like status
                    ivLike.setImageResource(R.drawable.ic_favorite_border); // Change to outline icon
                    Toast.makeText(this, "取消点赞", Toast.LENGTH_SHORT).show();
                } else {
                    // Not liked, like
                    currentNewsItem.setLikes(currentLikes + 1); // Increment likes
                    isNewsLiked = true; // Update like status
                    ivLike.setImageResource(R.drawable.ic_favorite_filled_red); // Change to filled red icon
                    Toast.makeText(this, "点赞 +1", Toast.LENGTH_SHORT).show();
                }
                tvViewsLikes.setText("浏览: " + currentNewsItem.getViews() + " 点赞: " + currentNewsItem.getLikes()); // Update displayed likes
                // TODO: Integrate with backend API for liking/unliking
            }
        });

        ivShare.setOnClickListener(v -> {
            // Implement share functionality
            if (currentNewsItem != null) {
                android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareMessage = currentNewsItem.getTitle() + "\n\n" + "在此处添加新闻链接或摘要"; // Customize share message
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                startActivity(android.content.Intent.createChooser(shareIntent, "分享新闻"));
            }
        });

        // 获取评论相关的控件
        rvComments = findViewById(R.id.rv_comments);
        etComment = findViewById(R.id.et_comment);
        btnSendComment = findViewById(R.id.btn_send_comment);

        // 设置评论列表
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new NewsCommentAdapter(commentList);
        rvComments.setAdapter(commentAdapter);

        // 添加静态评论数据 (for demonstration)
        commentList.add(new NewsComment("1", newsItem != null ? newsItem.getTitle() : "", "小邓", "2025-05-25 07:55", "这个新闻内容很有深度！", 5));
        commentList.add(new NewsComment("2", newsItem != null ? newsItem.getTitle() : "", "小方", "2025-05-25 07:54", "希望能有更多类似报道！", 3));
        commentAdapter.notifyDataSetChanged();

        // 设置发送评论按钮点击事件
        btnSendComment.setOnClickListener(v -> {
            String commentContent = etComment.getText().toString().trim();
            if (!commentContent.isEmpty()) {
                // TODO: Send comment to backend and add to list
                // For now, add as static data
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String currentTime = sdf.format(new Date());
                NewsComment newComment = new NewsComment(
                    String.valueOf(commentList.size() + 1), // Simple ID generation
                    newsItem != null ? newsItem.getTitle() : "",
                    "当前用户", // Replace with actual user name
                    currentTime,
                    commentContent,
                    0 // Initial likes
                );
                commentList.add(newComment);
                commentAdapter.notifyDataSetChanged();
                etComment.setText(""); // Clear input field
                // Optional: Scroll to the last comment
                rvComments.scrollToPosition(commentList.size() - 1);
            } else {
                Toast.makeText(this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfCollected() {
        if (currentNewsItem != null) {
            NewsDatabase.databaseWriteExecutor.execute(() -> {
                boolean isCollected = collectedNewsDao.isNewsCollected(currentNewsItem.getTitle()); // Using title as newsId
                runOnUiThread(() -> {
                    if (isCollected) {
                        ivCollect.setImageResource(R.drawable.ic_star_filled_yellow); // Change to filled yellow star icon
                    } else {
                        ivCollect.setImageResource(R.drawable.ic_star_border); // Change to outline star icon
                    }
                });
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // When returning from detail to list, pass back the updated like status and likes count
            Intent resultIntent = new Intent();
            if (currentNewsItem != null) {
                resultIntent.putExtra("newsTitle", currentNewsItem.getTitle());
                resultIntent.putExtra("updatedLikes", currentNewsItem.getLikes());
                resultIntent.putExtra("isLiked", isNewsLiked);
            }
            setResult(RESULT_OK, resultIntent);
            finish(); // Close detail activity
        }
        return super.onOptionsItemSelected(item);
    }
} 