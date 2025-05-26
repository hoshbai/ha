package com.example.campus_life_assistant;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.campus_life_assistant.entry.LostFoundItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LostFoundDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";
    
    private Toolbar toolbar;
    private TextView titleTextView;
    private TextView typeTextView;
    private TextView categoryTextView;
    private TextView timeTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;
    private TextView contactTextView;
    private TextView publishInfoTextView;
    private ImageView itemImageView;
    private Button contactButton;
    private Button completeButton;
    
    private LostFoundItem item;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found_detail);
        
        initViews();
        setupToolbar();
        
        // 获取传递的物品ID
        int itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            // 模拟根据ID获取物品数据
            item = getMockItemById(itemId);
            if (item != null) {
                updateUI();
            } else {
                Toast.makeText(this, "未找到物品信息", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.lost_found_detail_toolbar);
        titleTextView = findViewById(R.id.lost_found_detail_title);
        typeTextView = findViewById(R.id.lost_found_detail_type);
        categoryTextView = findViewById(R.id.lost_found_detail_category);
        timeTextView = findViewById(R.id.lost_found_detail_time);
        locationTextView = findViewById(R.id.lost_found_detail_location);
        descriptionTextView = findViewById(R.id.lost_found_detail_description);
        contactTextView = findViewById(R.id.lost_found_detail_contact);
        publishInfoTextView = findViewById(R.id.lost_found_detail_publisher);
        itemImageView = findViewById(R.id.lost_found_detail_image);
        contactButton = findViewById(R.id.lost_found_detail_contact_btn);
        completeButton = findViewById(R.id.lost_found_detail_complete_btn);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("详情");
    }
    
    private void updateUI() {
        titleTextView.setText(item.getTitle());
        
        // 设置物品类型和颜色
        typeTextView.setText(item.getItemTypeText());
        int typeColorResId = item.getItemType() == LostFoundItem.TYPE_LOST ? 
                R.color.color_lost : R.color.color_found;
        typeTextView.setBackgroundResource(typeColorResId);
        
        categoryTextView.setText(item.getCategory());
        timeTextView.setText(dateFormat.format(item.getTime()));
        locationTextView.setText(item.getLocation());
        descriptionTextView.setText(item.getDescription());
        
        // 设置联系方式
        String contactInfo = item.getContactType() + ": " + item.getContact();
        contactTextView.setText(contactInfo);
        
        // 设置发布信息
        String publishInfo = "发布者: " + item.getPublisherName();
        publishInfo += " | 发布时间: " + dateFormat.format(item.getPublishTime());
        publishInfoTextView.setText(publishInfo);
        
        // 根据物品类别设置对应的图片
        itemImageView.setImageResource(item.getItemImageResourceId());
        
        // 设置按钮状态
        if (item.isCompleted()) {
            completeButton.setText("已完成");
            completeButton.setEnabled(false);
        } else {
            String btnText = item.getItemType() == LostFoundItem.TYPE_LOST ? "已找到" : "已归还";
            completeButton.setText(btnText);
            completeButton.setEnabled(true);
        }
        
        // 设置按钮点击事件
        contactButton.setOnClickListener(v -> {
            Toast.makeText(this, "联系方式：" + contactInfo, Toast.LENGTH_LONG).show();
        });
        
        completeButton.setOnClickListener(v -> {
            item.setCompleted(true);
            updateUI();
            Toast.makeText(this, "状态已更新", Toast.LENGTH_SHORT).show();
        });
    }
    
    // 模拟根据ID获取物品数据
    private LostFoundItem getMockItemById(int id) {
        // 在实际应用中，应该从数据库或网络获取数据
        // 这里简单模拟一些数据
        switch (id) {
            case 1:
                return new LostFoundItem(
                        1,
                        "寻找学生证",
                        "在图书馆二楼自习室丢失学生证一张，姓名张三，学号2024001001，请捡到的同学联系我，万分感谢！",
                        "图书馆二楼",
                        new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L),
                        "证件",
                        "13812345678",
                        "电话",
                        null,
                        LostFoundItem.TYPE_LOST,
                        "张三",
                        "2024001001"
                );
            case 2:
                return new LostFoundItem(
                        2,
                        "寻找笔记本电脑",
                        "昨天在第一教学楼302教室上课时丢失联想ThinkPad笔记本电脑一台，黑色，贴有校徽贴纸。请捡到的同学联系我，重谢！",
                        "第一教学楼302",
                        new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L),
                        "电子",
                        "wx123456",
                        "微信",
                        null,
                        LostFoundItem.TYPE_LOST,
                        "李四",
                        "2024001002"
                );
            case 3:
                return new LostFoundItem(
                        3,
                        "拾到钱包一个",
                        "今天在食堂一楼捡到一个黑色钱包，内有现金和银行卡，请失主尽快联系我认领！",
                        "第一食堂",
                        new Date(System.currentTimeMillis()),
                        "钱包",
                        "qq123456789",
                        "QQ",
                        null,
                        LostFoundItem.TYPE_FOUND,
                        "王五",
                        "2024001003"
                );
            case 4:
                return new LostFoundItem(
                        4,
                        "拾到AirPods耳机",
                        "在体育馆篮球场捡到一副苹果AirPods耳机，白色，带充电盒。请失主联系我并说出充电盒背面刻字内容认领。",
                        "体育馆",
                        new Date(System.currentTimeMillis() - 3 * 60 * 60 * 1000L),
                        "电子",
                        "13987654321",
                        "电话",
                        null,
                        LostFoundItem.TYPE_FOUND,
                        "赵六",
                        "2024001004"
                );
            default:
                return null;
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