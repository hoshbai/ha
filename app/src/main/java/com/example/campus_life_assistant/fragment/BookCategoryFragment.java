package com.example.campus_life_assistant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.Adapter.BookAdapter;
import com.example.campus_life_assistant.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookCategoryFragment extends Fragment {

    public static final String ARG_CATEGORY_POSITION = "category_position";

    private int categoryPosition;
    private RecyclerView recyclerView;
    private List<Book> books = new ArrayList<>();

    // Sample book data for different categories
    private static final String[][] BOOK_DATA = {
            // 文学类
            {"百年孤独", "加西亚·马尔克斯", "文学", "4.8"},
            {"活着", "余华", "文学", "4.7"},
            {"红楼梦", "曹雪芹", "文学", "4.9"},
            {"围城", "钱钟书", "文学", "4.6"},
            {"平凡的世界", "路遥", "文学", "4.8"},
            {"追风筝的人", "卡勒德·胡赛尼", "文学", "4.7"},

            // 科技类
            {"时间简史", "史蒂芬·霍金", "科技", "4.6"},
            {"未来简史", "尤瓦尔·赫拉利", "科技", "4.7"},
            {"万物简史", "比尔·布莱森", "科技", "4.5"},
            {"硅谷钢铁侠", "阿什利·万斯", "科技", "4.5"},
            {"自私的基因", "理查德·道金斯", "科技", "4.6"},
            {"奇点临近", "雷·库兹韦尔", "科技", "4.4"},

            // 历史类
            {"人类简史", "尤瓦尔·赫拉利", "历史", "4.8"},
            {"全球通史", "斯塔夫里阿诺斯", "历史", "4.7"},
            {"明朝那些事儿", "当年明月", "历史", "4.7"},
            {"万历十五年", "黄仁宇", "历史", "4.6"},
            {"枪炮、病菌与钢铁", "贾雷德·戴蒙德", "历史", "4.8"},
            {"中国大历史", "黄仁宇", "历史", "4.5"},

            // 哲学类
            {"苏菲的世界", "乔斯坦·贾德", "哲学", "4.5"},
            {"西方哲学史", "伯特兰·罗素", "哲学", "4.6"},
            {"人生的智慧", "叔本华", "哲学", "4.4"},
            {"尼采：在世纪的转折点上", "周国平", "哲学", "4.3"},
            {"存在主义咖啡馆", "莎拉·贝克韦尔", "哲学", "4.2"},
            {"苏菲的哲学课", "乔斯坦·贾德", "哲学", "4.1"},

            // 艺术类
            {"艺术的故事", "贡布里希", "艺术", "4.7"},
            {"看的艺术", "约翰·伯格", "艺术", "4.4"},
            {"写给大家看的设计书", "Robin Williams", "艺术", "4.5"},
            {"艺术与视知觉", "鲁道夫·阿恩海姆", "艺术", "4.3"},
            {"梵高传", "欧文·斯通", "艺术", "4.6"},
            {"素描的诀窍", "伯特·多德森", "艺术", "4.2"},

            // 经济类
            {"国富论", "亚当·斯密", "经济", "4.7"},
            {"资本论", "卡尔·马克思", "经济", "4.8"},
            {"经济学原理", "曼昆", "经济", "4.5"},
            {"怪诞行为学", "丹·艾瑞里", "经济", "4.4"},
            {"思考，快与慢", "丹尼尔·卡尼曼", "经济", "4.6"},
            {"黑天鹅", "纳西姆·尼古拉斯·塔勒布", "经济", "4.5"},

            // 自然科学类
            {"自然探险家", "戴维·阿滕伯勒", "自然科学", "4.5"},
            {"物种起源", "查尔斯·达尔文", "自然科学", "4.7"},
            {"寂静的春天", "蕾切尔·卡森", "自然科学", "4.4"},
            {"宇宙的琴弦", "布莱恩·格林", "自然科学", "4.6"},
            {"地球的法则", "詹姆斯·洛夫洛克", "自然科学", "4.3"},
            {"七堂极简物理课", "卡洛·罗韦利", "自然科学", "4.4"},

            // 计算机类
            {"深入理解计算机系统", "Randal E. Bryant", "计算机", "4.9"},
            {"代码大全", "Steve McConnell", "计算机", "4.7"},
            {"算法导论", "Thomas H. Cormen", "计算机", "4.8"},
            {"设计模式", "Erich Gamma", "计算机", "4.6"},
            {"程序员修炼之道", "Andrew Hunt", "计算机", "4.5"},
            {"Java编程思想", "Bruce Eckel", "计算机", "4.7"},

            // 医学类
            {"本草纲目", "李时珍", "医学", "4.8"},
            {"黄帝内经", "佚名", "医学", "4.7"},
            {"营养圣经", "帕特里克·霍尔福德", "医学", "4.3"},
            {"临床诊断学", "陈文彬", "医学", "4.5"},
            {"中医学基础", "喻嘉言", "医学", "4.4"},
            {"医学的艺术", "伯纳德·劳恩", "医学", "4.2"}
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryPosition = getArguments().getInt(ARG_CATEGORY_POSITION, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.booksRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Load books based on category
        loadBooksForCategory();

        // Set adapter
        BookAdapter adapter = new BookAdapter(getContext(), books);
        recyclerView.setAdapter(adapter);
    }

    private void loadBooksForCategory() {
        books.clear();
        Random random = new Random();

        // For "All Books" category, add books from all categories
        if (categoryPosition == 0) {
            for (String[] bookData : BOOK_DATA) {
                String imageUrl = "https://placeholder.com/book" + (random.nextInt(20) + 1) + ".jpg";
                books.add(new Book(
                        bookData[0],
                        bookData[1],
                        bookData[2],
                        imageUrl,
                        Float.parseFloat(bookData[3])
                ));
            }
        } else {
            // For specific categories, add only books from that category
            int startIndex = (categoryPosition - 1) * 6;
            int endIndex = Math.min(startIndex + 6, BOOK_DATA.length);

            for (int i = startIndex; i < endIndex; i++) {
                String[] bookData = BOOK_DATA[i];
                String imageUrl = "https://placeholder.com/book" + (random.nextInt(20) + 1) + ".jpg";
                books.add(new Book(
                        bookData[0],
                        bookData[1],
                        bookData[2],
                        imageUrl,
                        Float.parseFloat(bookData[3])
                ));
            }

            // Add a few more random books to make the category look populated
            for (int i = 0; i < 6; i++) {
                int randomIndex = random.nextInt(BOOK_DATA.length);
                String[] bookData = BOOK_DATA[randomIndex];
                String imageUrl = "https://placeholder.com/book" + (random.nextInt(20) + 1) + ".jpg";
                books.add(new Book(
                        bookData[0],
                        bookData[1],
                        bookData[2],
                        imageUrl,
                        Float.parseFloat(bookData[3])
                ));
            }
        }
    }
}