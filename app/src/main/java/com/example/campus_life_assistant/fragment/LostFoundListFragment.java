package com.example.campus_life_assistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.campus_life_assistant.Adapter.LostFoundAdapter;
import com.example.campus_life_assistant.AddLostFoundActivity;
import com.example.campus_life_assistant.LostFoundActivity;
import com.example.campus_life_assistant.LostFoundDetailActivity;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.entry.LostFoundItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LostFoundListFragment extends Fragment implements LostFoundAdapter.OnItemClickListener {

    private static final String ARG_ITEM_TYPE = "item_type";
    private static List<LostFoundItem> allItems = new ArrayList<>(); // 存储所有物品的静态列表
    private static int nextId = 5; // 下一个可用的ID，从5开始（因为示例数据用了1-4）
    
    private Integer itemType;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton addFab;
    private LostFoundAdapter adapter;
    private List<LostFoundItem> itemList = new ArrayList<>();

    public static LostFoundListFragment newInstance(Integer itemType) {
        LostFoundListFragment fragment = new LostFoundListFragment();
        Bundle args = new Bundle();
        if (itemType != null) {
            args.putInt(ARG_ITEM_TYPE, itemType);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 解析参数
        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_TYPE)) {
            itemType = getArguments().getInt(ARG_ITEM_TYPE);
        }
        
        // 如果静态列表为空，初始化示例数据
        if (allItems.isEmpty()) {
            allItems.addAll(generateMockItems());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_found, container, false);
        
        // 初始化视图
        recyclerView = view.findViewById(R.id.lost_found_recycler_view);
        emptyView = view.findViewById(R.id.lost_found_empty_view);
        swipeRefreshLayout = view.findViewById(R.id.lost_found_swipe_refresh);
        addFab = view.findViewById(R.id.lost_found_add_fab);
        
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        
        // 设置添加按钮点击事件
        addFab.setOnClickListener(v -> {
            // 跳转到添加失物招领信息的页面
            Intent intent = new Intent(getActivity(), AddLostFoundActivity.class);
            startActivityForResult(intent, 1); // 使用startActivityForResult而不是startActivity
        });
        
        // 初始化适配器
        adapter = new LostFoundAdapter(getContext(), itemList, this);
        recyclerView.setAdapter(adapter);
        
        // 加载数据
        loadData();
        
        return view;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // 无论结果如何，都刷新数据
            refreshData();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // 页面回到前台时刷新数据
        refreshData();
    }
    
    // 加载数据
    private void loadData() {
        if (getActivity() instanceof LostFoundActivity) {
            ((LostFoundActivity) getActivity()).showLoading();
        }
        
        // 模拟网络请求延迟
        new Handler().postDelayed(() -> {
            // 根据类型筛选数据
            List<LostFoundItem> filteredItems = new ArrayList<>();
            for (LostFoundItem item : allItems) {
                if (itemType == null || item.getItemType() == itemType) {
                    filteredItems.add(item);
                }
            }
            
            // 更新UI
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    updateUI(filteredItems);
                    if (getActivity() instanceof LostFoundActivity) {
                        ((LostFoundActivity) getActivity()).hideLoading();
                    }
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }, 500); // 延迟0.5秒模拟网络请求
    }
    
    // 刷新数据
    private void refreshData() {
        loadData();
    }
    
    // 更新UI
    private void updateUI(List<LostFoundItem> items) {
        itemList.clear();
        itemList.addAll(items);
        adapter.notifyDataSetChanged();
        
        // 处理空视图
        if (items.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    // 添加新物品
    public static void addItem(LostFoundItem item) {
        item.setId(nextId++);
        allItems.add(0, item); // 添加到列表开头
    }
    
    // 生成模拟数据
    private List<LostFoundItem> generateMockItems() {
        List<LostFoundItem> mockItems = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        
        // 寻物启事模拟数据
        if (itemType == null || itemType == LostFoundItem.TYPE_LOST) {
            // 校园卡
            calendar.add(Calendar.DAY_OF_MONTH, -2);
            Date time1 = calendar.getTime();
            mockItems.add(new LostFoundItem(
                    1,
                    "寻找学生证",
                    "在图书馆二楼自习室丢失学生证一张，姓名张三，学号2024001001，请捡到的同学联系我，万分感谢！",
                    "图书馆二楼",
                    time1,
                    "证件",
                    "13812345678",
                    "电话",
                    null,
                    LostFoundItem.TYPE_LOST,
                    "张三",
                    "2024001001"
            ));
            
            // 笔记本电脑
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date time2 = calendar.getTime();
            mockItems.add(new LostFoundItem(
                    2,
                    "寻找笔记本电脑",
                    "昨天在第一教学楼302教室上课时丢失联想ThinkPad笔记本电脑一台，黑色，贴有校徽贴纸。请捡到的同学联系我，重谢！",
                    "第一教学楼302",
                    time2,
                    "电子",
                    "wx123456",
                    "微信",
                    null,
                    LostFoundItem.TYPE_LOST,
                    "李四",
                    "2024001002"
            ));
        }
        
        // 招领启事模拟数据
        if (itemType == null || itemType == LostFoundItem.TYPE_FOUND) {
            // 钱包
            calendar.setTime(new Date());
            Date time3 = calendar.getTime();
            mockItems.add(new LostFoundItem(
                    3,
                    "拾到钱包一个",
                    "今天在食堂一楼捡到一个黑色钱包，内有现金和银行卡，请失主尽快联系我认领！",
                    "第一食堂",
                    time3,
                    "钱包",
                    "qq123456789",
                    "QQ",
                    null,
                    LostFoundItem.TYPE_FOUND,
                    "王五",
                    "2024001003"
            ));
            
            // AirPods耳机
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR, -3);
            Date time4 = calendar.getTime();
            mockItems.add(new LostFoundItem(
                    4,
                    "拾到AirPods耳机",
                    "在体育馆篮球场捡到一副苹果AirPods耳机，白色，带充电盒。请失主联系我并说出充电盒背面刻字内容认领。",
                    "体育馆",
                    time4,
                    "电子",
                    "13987654321",
                    "电话",
                    null,
                    LostFoundItem.TYPE_FOUND,
                    "赵六",
                    "2024001004"
            ));
        }
        
        return mockItems;
    }

    // 根据ID获取物品
    public static LostFoundItem getItemById(int id) {
        for (LostFoundItem item : allItems) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void onItemClick(LostFoundItem item) {
        // 跳转到详情页面
        Intent intent = new Intent(getActivity(), LostFoundDetailActivity.class);
        intent.putExtra(LostFoundDetailActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onContactClick(LostFoundItem item) {
        String message = "联系方式：" + item.getContactType() + " " + item.getContact();
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompleteClick(LostFoundItem item, int position) {
        String message = item.getItemType() == LostFoundItem.TYPE_LOST ? 
                "恭喜你找回了物品！" : "物品已成功归还失主！";
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        adapter.updateItemStatus(position, true);
    }
} 