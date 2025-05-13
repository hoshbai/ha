package com.example.campus_life_assistant.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.campus_life_assistant.Adapter.EventAdapter;
import com.example.campus_life_assistant.EventDetailActivity;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.ViewModel.EventViewModel;
import com.example.campus_life_assistant.entry.CampusEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventListFragment extends Fragment implements EventAdapter.OnEventClickListener {

    private static final String ARG_CATEGORY = "category";
    
    private String category;
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<CampusEvent> eventList = new ArrayList<>();
    private TextView emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EventViewModel viewModel;

    public EventListFragment() {
        // Required empty public constructor
    }

    public static EventListFragment newInstance(String category) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
        
        // 此处应注入ViewModel，但当前简化为示例数据
        // viewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerView = view.findViewById(R.id.event_recycler_view);
        emptyView = view.findViewById(R.id.empty_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        
        // 设置适配器
        adapter = new EventAdapter(getContext(), eventList, this);
        recyclerView.setAdapter(adapter);
        
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(this::loadEvents);
        
        // 加载事件数据
        loadEvents();
    }
    
    private void loadEvents() {
        // 在真实实现中，这里应当从ViewModel获取数据
        // 现在用模拟数据代替
        swipeRefreshLayout.setRefreshing(true);
        
        // 清空现有列表
        eventList.clear();
        
        // 添加模拟数据
        eventList.addAll(generateMockEvents(category));
        
        // 更新UI
        adapter.notifyDataSetChanged();
        updateEmptyView();
        swipeRefreshLayout.setRefreshing(false);
    }
    
    private void updateEmptyView() {
        if (eventList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onEventClick(CampusEvent event) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }
    
    @Override
    public void onRegisterClick(CampusEvent event, int position) {
        // 在真实实现中，这里应该调用ViewModel进行报名操作
        // 现在仅模拟成功报名
        Toast.makeText(getContext(), "报名成功！", Toast.LENGTH_SHORT).show();
        adapter.updateEventStatus(position, true);
    }
    
    // 生成模拟数据的辅助方法
    private List<CampusEvent> generateMockEvents(String category) {
        List<CampusEvent> mockEvents = new ArrayList<>();
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        
        // 添加符合当前分类的模拟数据
        if ("全部".equals(category) || "讲座".equals(category)) {
            calendar.add(Calendar.DAY_OF_MONTH, 2);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.HOUR, 2);
            Date endTime = calendar.getTime();
            
            mockEvents.add(new CampusEvent(
                    1, 
                    "人工智能前沿技术讲座", 
                    "本次讲座将邀请知名AI专家，深入探讨人工智能领域的最新研究进展和应用案例。讲座将涵盖机器学习、深度学习、自然语言处理等热门话题，并讨论AI技术在各行业的落地应用。欢迎对AI技术感兴趣的同学参加！",
                    "计算机科学楼报告厅",
                    startTime,
                    endTime,
                    "计算机科学学院",
                    "https://example.com/ai_seminar.jpg",
                    "讲座"
            ));
        }
        
        if ("全部".equals(category) || "文娱".equals(category)) {
            calendar.setTime(new Date()); // 重置时间
            calendar.add(Calendar.DAY_OF_MONTH, 5);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.HOUR, 3);
            Date endTime = calendar.getTime();
            
            mockEvents.add(new CampusEvent(
                    2, 
                    "校园歌手大赛决赛", 
                    "校园歌手大赛决赛即将举行！经过前期的层层选拔，10位优秀选手将在决赛舞台上一决高下。现场将有专业评委点评，并邀请知名音乐人助阵。欢迎广大师生前来观看，为你喜爱的选手加油！",
                    "大学生活动中心音乐厅",
                    startTime,
                    endTime,
                    "校团委学生会",
                    "https://example.com/singing_contest.jpg",
                    "文娱"
            ));
        }
        
        if ("全部".equals(category) || "体育".equals(category)) {
            calendar.setTime(new Date()); // 重置时间
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.HOUR, 4);
            Date endTime = calendar.getTime();
            
            mockEvents.add(new CampusEvent(
                    3, 
                    "校园篮球联赛", 
                    "一年一度的校园篮球联赛又要开始啦！本次比赛分为男子组和女子组，各学院代表队将进行激烈角逐。比赛采用小组赛+淘汰赛制度，冠军队伍将获得丰厚奖品。欢迎各学院组织啦啦队，为自己的学院加油！",
                    "校体育馆",
                    startTime,
                    endTime,
                    "体育部",
                    "https://example.com/basketball_league.jpg",
                    "体育"
            ));
        }
        
        if ("全部".equals(category) || "竞赛".equals(category)) {
            calendar.setTime(new Date()); // 重置时间
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1); // 持续一天
            Date endTime = calendar.getTime();
            
            mockEvents.add(new CampusEvent(
                    4, 
                    "大学生创新创业大赛", 
                    "第十届大学生创新创业大赛即将开始报名！本次大赛旨在培养学生的创新精神和创业能力，设置了创意组、初创组和成长组三个组别。优秀项目将有机会获得创业基金支持和创业导师指导。欢迎有创意的同学踊跃报名参加！",
                    "创新创业中心",
                    startTime,
                    endTime,
                    "创新创业学院",
                    "https://example.com/innovation_competition.jpg",
                    "竞赛"
            ));
        }
        
        if ("全部".equals(category) || "实践".equals(category)) {
            calendar.setTime(new Date()); // 重置时间
            calendar.add(Calendar.DAY_OF_MONTH, 10);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 5); // 持续5天
            Date endTime = calendar.getTime();
            
            mockEvents.add(new CampusEvent(
                    5, 
                    "暑期社会实践活动", 
                    "2024年暑期社会实践活动报名开始啦！本次实践活动分为\"乡村振兴\"、\"科技支教\"、\"环保调研\"三大主题。参与者将深入基层，了解国情民情，锻炼实践能力。活动结束后，优秀团队将获得表彰，并有机会参加全国交流会。",
                    "校内各学院",
                    startTime,
                    endTime,
                    "校团委",
                    "https://example.com/social_practice.jpg",
                    "实践"
            ));
        }
        
        return mockEvents;
    }
} 