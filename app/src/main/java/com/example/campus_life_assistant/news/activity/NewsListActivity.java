package com.example.campus_life_assistant.news.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.SearchView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.news.api.ApiService;
import com.example.campus_life_assistant.news.api.RetrofitInstance;
import com.example.campus_life_assistant.news.adapter.NewsAdapter;
import com.example.campus_life_assistant.news.model.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.content.Intent;

public class NewsListActivity extends AppCompatActivity {

    private static final String TAG = "NewsListActivity";
    private RecyclerView rvNewsList;
    private NewsAdapter newsAdapter;
    private List<NewsItem> newsList = new ArrayList<>();
    private List<NewsItem> filteredNewsList = new ArrayList<>(); // 用于存储过滤后的新闻列表
    private SearchView svNewsSearch;
    private ActivityResultLauncher<Intent> detailActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("校园新闻"); // Set initial title

        rvNewsList = findViewById(R.id.rv_news_list);
        rvNewsList.setLayoutManager(new LinearLayoutManager(this));

        newsAdapter = new NewsAdapter(filteredNewsList); // Use filtered list for adapter
        rvNewsList.setAdapter(newsAdapter);

        detailActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String newsTitle = data.getStringExtra("newsTitle");
                            int updatedLikes = data.getIntExtra("updatedLikes", -1);
                            boolean isLiked = data.getBooleanExtra("isLiked", false);

                            if (newsTitle != null && updatedLikes != -1) {
                                for (NewsItem newsItem : newsList) {
                                    if (newsItem.getTitle().equals(newsTitle)) {
                                        newsItem.setLikes(updatedLikes);
                                        if (newsAdapter != null) {
                                            newsAdapter.setNewsLikedStatus(newsTitle, isLiked);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
        );

        // 设置新闻列表项点击事件
        newsAdapter.setOnNewsItemClickListener(newsItem -> {
            // TODO: 跳转到新闻详情页
            Toast.makeText(this, "点击了: " + newsItem.getTitle(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, com.example.campus_life_assistant.news.activity.NewsDetailActivity.class);
            intent.putExtra("newsItem", newsItem);
            intent.putExtra("isLiked", newsAdapter.likedNewsTitles.contains(newsItem.getTitle()));

            detailActivityResultLauncher.launch(intent);
        });

        // 获取 SearchView 实例
        svNewsSearch = findViewById(R.id.sv_news_search);
        svNewsSearch.setIconifiedByDefault(true); // Make it an icon by default
        setupSearchView(); // Setup search view listeners

        // 加载新闻数据
        loadNews();
    }

    private void loadNews() {
        // 使用静态数据
        newsList.clear();

        newsList.add(new NewsItem(
            "校园文化节圆满落幕",
            "校团委",
            "2025-05-20",
            "xywhj", // 使用本地图片资源名称
            128,
            1024,
            "校园文化节于本周末在校体育馆圆满落幕。为期三天的文化节吸引了全校师生的热情参与，带来了丰富多彩的文化体验。\n\n今年的文化节以\"青春飞扬，文化传承\"为主题，活动内容涵盖了文艺表演、书画展览、传统手工艺体验、美食文化交流等多个方面。开幕式上，校领导发表了热情洋溢的致辞，强调了校园文化建设的重要性。\n\n在文艺表演环节，学生社团带来了精彩的歌舞、戏剧、乐器演奏等节目，展现了新时代大学生的青春活力和艺术才华。书画展览区展出了师生们的优秀作品，笔墨丹青间流淌着深厚的文化底蕴。传统手工艺体验区则让同学们亲手制作剪纸、泥塑、香囊等，感受中华优秀传统文化的魅力。\n\n美食文化交流区更是人气爆棚，来自各地的学生带来了家乡特色美食，让大家在品尝美味的同时，增进了对不同地域文化的了解。闭幕式上，对在各项活动中表现突出的集体和个人进行了表彰。\n\n校园文化节的成功举办，不仅丰富了校园文化生活，也为师生们提供了一个展示自我、交流互动的平台，进一步增强了校园的凝聚力和向心力。期待明年的校园文化节更加精彩！"
        ));

        newsList.add(new NewsItem(
            "我校在全国大学生创新创业大赛中获佳绩",
            "教务处",
            "2025-05-10",
            "dac", // 使用本地图片资源名称
            256,
            2048,
            "我校代表队在刚刚结束的全国大学生创新创业大赛中荣获佳绩，斩获多项金奖和银奖，为学校赢得了荣誉。\n\n本次大赛汇聚了全国各地的优秀大学生创新创业项目，竞争异常激烈。我校高度重视此次比赛，组织了由多个学院组成的代表队，参赛项目涵盖了人工智能、生物科技、环境保护、文化创意等多个领域。\n\n在比赛过程中，我校代表队凭借扎实的专业知识、创新的项目理念和出色的团队协作，赢得了评委们的高度评价。多个项目在各自领域展现出前沿性和应用前景，受到了投资人和企业的广泛关注。\n\n学校领导对参赛师生表示祝贺，并鼓励他们继续发扬创新精神，将科研成果转化为实际应用，服务社会经济发展。此次大赛的成功，充分展示了我校在创新创业教育方面的成果，也为培养更多高素质创新型人才奠定了坚实基础。\n\n学校将继续加大对创新创业教育的投入，为学生提供更优质的平台和资源，鼓励更多学生投身创新创业实践，为实现中华民族伟大复兴的中国梦贡献青春力量。"
        ));

        newsList.add(new NewsItem(
            "图书馆新增电子资源数据库",
            "图书馆",
            "2025-04-1",
            "tsg", // 使用本地图片资源名称
            64,
            512,
            "为了进一步丰富我校师生的学习和研究资源，图书馆近期成功引进了多个重要的电子资源数据库，现已面向全校开放使用。\n\n新增数据库涵盖了自然科学、社会科学、人文历史、工程技术等多个学科领域，包括知名学术期刊、会议论文、学位论文、电子图书、行业报告等多种类型。这些资源的引入将极大地便利师生们进行文献查阅、课题研究、论文撰写等工作。\n\n图书馆提供了详细的数据库使用指南和在线培训课程，帮助师生们更好地利用这些资源。同时，图书馆网站也已更新了数据库列表和访问链接，师生们可以通过校园网或VPN远程访问。\n\n图书馆将继续关注学术前沿和师生需求，不断优化资源结构，为学校的教学科研提供有力支撑。欢迎广大师生积极使用新引进的电子资源数据库，如有任何问题或建议，请及时与图书馆联系。"
        ));

        newsList.add(new NewsItem(
            "校园招聘会成功举办",
            "就业指导中心",
            "2025-03-17",
            "xyzph", // 使用本地图片资源名称
            32,
            256,
            "由我校就业指导中心主办的校园招聘会于今日成功举办，吸引了众多知名企业和应届毕业生参加，现场气氛热烈，交流活跃。\n\n本次招聘会涵盖了信息技术、金融、教育、制造、服务等多个行业，共有100余家企业提供了超过3000个就业岗位。招聘会现场设置了企业展位、宣讲区、简历诊断区等功能区域，为毕业生提供了全方位的求职服务。\n\n招聘会吸引了来自本校及周边高校的近万名毕业生前来求职。同学们手持简历，积极与企业代表交流，了解岗位信息和企业文化。许多企业表示，对我校毕业生的综合素质和专业能力给予高度评价。\n\n就业指导中心在招聘会期间还组织了多场就业指导讲座和模拟面试活动，帮助毕业生提升求职技巧。本次招聘会的成功举办，为我校毕业生提供了宝贵的就业机会，也搭建了学校与企业之间良好的沟通平台。\n\n就业指导中心将继续为毕业生提供持续的就业指导和帮扶，助力他们顺利走上工作岗位。"
        ));

        newsList.add(new NewsItem(
            "我校与知名企业签署校企合作协议",
            "对外合作处",
            "2025-03-10",
            "xqhz", // 使用本地图片资源名称
            96,
            768,
            "我校今日与国内知名科技企业[科大讯飞]成功签署了校企合作协议，双方将在人才培养、科研创新、实习实践等领域展开深度合作。\n\n此次合作是我校推进产教融合、协同育人的重要举措，旨在优势互补、资源共享，共同培养适应行业发展需求的高素质人才。根据协议，双方将共建联合实验室、开展科研项目合作、设立学生实习基地、共同开发课程等。\n\n[企业名称]负责人表示，对与我校的合作充满信心，期待通过校企合作，为企业输送更多优秀人才，共同推动科技创新和产业升级。学校领导表示，此次合作将为学生提供更多实践机会和就业平台，提升学校的办学水平和社会服务能力。\n\n未来，双方将定期举行沟通交流会，不断深化合作内容，拓展合作领域，共同探索校企合作新模式，为国家创新驱动发展战略贡献力量。"
        ));

        filteredNewsList.clear();
        filteredNewsList.addAll(newsList);
        newsAdapter.notifyDataSetChanged();
    }

    private void setupSearchView() {
        svNewsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNewsList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNewsList(newText);
                return false;
            }
        });

        svNewsSearch.setOnCloseListener(() -> {
            // When search is closed, show all news and original title
            filteredNewsList.clear();
            filteredNewsList.addAll(newsList);
            newsAdapter.notifyDataSetChanged();
            // getSupportActionBar().setTitle("校园新闻"); // Restore original title -SearchView handles title when expanded/collapsed
            return false;
        });
    }

    private void filterNewsList(String query) {
        filteredNewsList.clear();
        if (query.isEmpty()) {
            filteredNewsList.addAll(newsList);
        } else {
            query = query.toLowerCase(Locale.getDefault());
            for (NewsItem news : newsList) {
                if (news.getTitle().toLowerCase(Locale.getDefault()).contains(query) ||
                    news.getPublisher().toLowerCase(Locale.getDefault()).contains(query) /* Add other fields to search */) {
                    filteredNewsList.add(news);
                }
            }
        }
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 