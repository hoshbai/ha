package com.example.campus_life_assistant;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.campus_life_assistant.entry.LostFoundItem;
import com.example.campus_life_assistant.fragment.LostFoundListFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LostFoundDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";

    private Toolbar toolbar;
    private ImageView imageView;
    private TextView titleText;
    private TextView typeText;
    private TextView timeText;
    private TextView locationText;
    private TextView categoryText;
    private TextView descriptionText;
    private TextView contactText;
    private TextView publisherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found_detail);

        initViews();
        setupToolbar();

        // 获取物品ID
        int itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId == -1) {
            Toast.makeText(this, "物品信息不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 从LostFoundListFragment获取物品信息
        LostFoundItem item = LostFoundListFragment.getItemById(itemId);
        if (item == null) {
            Toast.makeText(this, "物品信息不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 显示物品信息
        displayItemInfo(item);
    }

    private void initViews() {
        toolbar = findViewById(R.id.lost_found_detail_toolbar);
        imageView = findViewById(R.id.lost_found_detail_image);
        titleText = findViewById(R.id.lost_found_detail_title);
        typeText = findViewById(R.id.lost_found_detail_type);
        timeText = findViewById(R.id.lost_found_detail_time);
        locationText = findViewById(R.id.lost_found_detail_location);
        categoryText = findViewById(R.id.lost_found_detail_category);
        descriptionText = findViewById(R.id.lost_found_detail_description);
        contactText = findViewById(R.id.lost_found_detail_contact);
        publisherText = findViewById(R.id.lost_found_detail_publisher);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("物品详情");
    }

    private void displayItemInfo(LostFoundItem item) {
        // 设置标题
        titleText.setText(item.getTitle());

        // 设置类型
        String typeString = item.getItemType() == LostFoundItem.TYPE_LOST ? "寻物启事" : "招领启事";
        typeText.setText(typeString);

        // 设置时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        timeText.setText(sdf.format(item.getTime()));

        // 设置地点
        locationText.setText(item.getLocation());

        // 设置分类
        categoryText.setText(item.getCategory());

        // 设置描述
        descriptionText.setText(item.getDescription());

        // 设置联系方式
        String contactString = String.format("%s: %s", item.getContactType(), item.getContact());
        contactText.setText(contactString);

        // 设置发布者
        String publisherString = String.format("发布者: %s", item.getPublisherName());
        publisherText.setText(publisherString);

        // 设置图片
        if (item.getCategory().equals("证件")) {
            imageView.setImageResource(R.drawable.ic_lost_found_card);
        } else if (item.getCategory().equals("电子")) {
            imageView.setImageResource(R.drawable.ic_lost_found_electronic);
        } else if (item.getCategory().equals("钱包")) {
            imageView.setImageResource(R.drawable.ic_lost_found_wallet);
        } else {
            imageView.setImageResource(R.drawable.ic_lost_found_other);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 