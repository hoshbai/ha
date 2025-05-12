package com.example.campus_life_assistant;

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
import com.example.campus_life_assistant.Adapter.LibraryNotificationAdapter;
import com.example.campus_life_assistant.Adapter.LibraryTabPagerAdapter;
import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.model.LibraryNotification;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private RecyclerView recommendationsRecyclerView;
    private RecyclerView notificationsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("图书馆");

        // Initialize ViewPager and TabLayout
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Setup tabs and ViewPager
        setupTabsAndViewPager();

        // Setup daily recommendations
        recommendationsRecyclerView = findViewById(R.id.dailyRecommendationsRecyclerView);
        setupDailyRecommendations();

        // Setup notifications
        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);
        setupNotifications();

        // Setup quick actions
        setupQuickActions();
    }

    private void setupTabsAndViewPager() {
        LibraryTabPagerAdapter adapter = new LibraryTabPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Connect TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("全部图书");
                            break;
                        case 1:
                            tab.setText("文学");
                            break;
                        case 2:
                            tab.setText("科技");
                            break;
                        case 3:
                            tab.setText("历史");
                            break;
                        case 4:
                            tab.setText("哲学");
                            break;
                        case 5:
                            tab.setText("艺术");
                            break;
                        case 6:
                            tab.setText("经济");
                            break;
                        case 7:
                            tab.setText("自然科学");
                            break;
                        case 8:
                            tab.setText("计算机");
                            break;
                        case 9:
                            tab.setText("医学");
                            break;
                    }
                }
        ).attach();
    }

    private void setupDailyRecommendations() {
        // Create sample book data
        List<Book> recommendedBooks = Arrays.asList(
                new Book("深入理解计算机系统", "Randal E. Bryant", "计算机", "https://placeholder.com/book1.jpg", 4.9f),
                new Book("活着", "余华", "文学", "https://placeholder.com/book2.jpg", 4.8f),
                new Book("人类简史", "尤瓦尔·赫拉利", "历史", "https://placeholder.com/book3.jpg", 4.7f),
                new Book("三体", "刘慈欣", "科幻", "https://placeholder.com/book4.jpg", 4.9f),
                new Book("算法导论", "Thomas H. Cormen", "计算机", "https://placeholder.com/book5.jpg", 4.6f)
        );

        BookAdapter adapter = new BookAdapter(this, recommendedBooks);
        recommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendationsRecyclerView.setAdapter(adapter);
    }

    private void setupNotifications() {
        // Create sample notification data
        List<LibraryNotification> notifications = new ArrayList<>();
        notifications.add(new LibraryNotification(
                LibraryNotification.TYPE_DUE_SOON,
                "《设计模式》将于3天后到期",
                "请及时续借或归还图书，以免产生罚款",
                System.currentTimeMillis()));
        notifications.add(new LibraryNotification(
                LibraryNotification.TYPE_OVERDUE,
                "《Java编程思想》已逾期2天",
                "当前罚款: ¥2.00，请尽快归还",
                System.currentTimeMillis() - 86400000));
        notifications.add(new LibraryNotification(
                LibraryNotification.TYPE_ANNOUNCEMENT,
                "图书馆开放时间调整通知",
                "5月1日至5月3日期间，图书馆开放时间调整为9:00-17:00",
                System.currentTimeMillis() - 172800000));

        LibraryNotificationAdapter adapter = new LibraryNotificationAdapter(this, notifications);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationsRecyclerView.setAdapter(adapter);
    }

    private void setupQuickActions() {
        // Setup click listeners for quick action buttons
        findViewById(R.id.btnBorrowedBooks).setOnClickListener(v -> {
            Toast.makeText(this, "查看已借阅图书", Toast.LENGTH_SHORT).show();
            // TODO: 跳转到已借阅图书页面
        });

        findViewById(R.id.btnWishlist).setOnClickListener(v -> {
            Toast.makeText(this, "查看我的书单", Toast.LENGTH_SHORT).show();
            // TODO: 跳转到我的书单页面
        });

        findViewById(R.id.btnReservations).setOnClickListener(v -> {
            Toast.makeText(this, "查看预约记录", Toast.LENGTH_SHORT).show();
            // TODO: 跳转到预约记录页面
        });

        findViewById(R.id.btnReadingHistory).setOnClickListener(v -> {
            Toast.makeText(this, "查看阅读历史", Toast.LENGTH_SHORT).show();
            // TODO: 跳转到阅读历史页面
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