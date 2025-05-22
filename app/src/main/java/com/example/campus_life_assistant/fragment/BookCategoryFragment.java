package com.example.campus_life_assistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.Adapter.BookAdapter;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.activity.BookDetailActivity;
import com.example.campus_life_assistant.model.Book;
import com.example.campus_life_assistant.network.ApiService;
import com.example.campus_life_assistant.network.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookCategoryFragment extends Fragment {

    private static final String TAG = "BookCategoryFragment";
    public static final String ARG_CATEGORY_POSITION = "category_position";

    // 页面组件
    private int categoryPosition;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private ApiService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取分类位置参数
        if (getArguments() != null) {
            categoryPosition = getArguments().getInt(ARG_CATEGORY_POSITION, 0);
        }
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_category, container, false);

        // 初始化视图组件
        recyclerView = view.findViewById(R.id.booksRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        emptyTextView = view.findViewById(R.id.emptyTextView);

        setupRecyclerView();
        loadBooksFromApi(); // 重要：触发数据加载

        return view;
    }

    // 设置RecyclerView布局
    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    // 核心方法：从API加载数据
    private void loadBooksFromApi() {
        showLoading();

        // 根据分类位置决定请求方式
        Call<List<Book>> call = (categoryPosition == 0)
                ? apiService.getAllBooks()
                : apiService.getBooksByCategory(getCategoryName());

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    handleEmptyData();
                    logErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                hideLoading();
                showNetworkError(t.getMessage());
                Log.e(TAG, "网络请求失败: ", t);
            }
        });
    }

    // 更新UI显示数据
    private void updateUI(List<Book> books) {
        if (books.isEmpty()) {
            handleEmptyData();
        } else {
            emptyTextView.setVisibility(View.GONE);
            BookAdapter adapter = new BookAdapter(requireContext(), books);

            // 设置点击监听（关键修复点）
            adapter.setOnItemClickListener(bookId -> {
                Log.d(TAG, "正在获取图书详情，ID: " + bookId);
                fetchBookDetails(bookId);
            });

            recyclerView.setAdapter(adapter);
        }
    }

    // 获取图书详情
    private void fetchBookDetails(int bookId) {
        String endpoint = "api/library/" + bookId; // 根据ApiService定义
        String requestType = (categoryPosition == 0) ? "全部图书" : getCategoryName();
        Log.d(TAG, "正在请求分类：" + requestType
                + "\n完整路径：" + RetrofitClient.BASE_URL + "请求方法路径");

        apiService.getBookDetails(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToDetail(response.body());
                } else {
                    handleDetailError(response);
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                showNetworkError(t.getMessage());
                Log.e(TAG, "详情请求失败: ", t);
            }
        });
    }

    // 跳转到详情页（关键修复点）
    private void navigateToDetail(Book book) {
        Intent intent = new Intent(requireActivity(), BookDetailActivity.class);
        intent.putExtra("book_data", book); // 使用Parcelable传递
        startActivity(intent);
    }

    //----- 工具方法 -----------------------------------------------
    private String getCategoryName() {
        final String[] CATEGORIES = {
                "全部",
                "小说·文学",
                "科技·IT·互联网",
                "历史·传记",
                "诗歌·散文",
                "艺术·设计·摄影",
                "经济·金融",
                "科学·自然",
                "医学·健康·养生"
        };
        return (categoryPosition < CATEGORIES.length)
                ? CATEGORIES[categoryPosition]
                : "全部";
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void handleEmptyData() {
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText("没有找到相关图书");
    }

    private void logErrorResponse(Response<?> response) {
        try {
            String error = response.errorBody() != null
                    ? response.errorBody().string()
                    : "未知错误";
            Log.e(TAG, "请求失败: " + response.code() + " - " + error);
        } catch (IOException e) {
            Log.e(TAG, "错误信息解析失败", e);
        }
    }

    private void handleDetailError(Response<Book> response) {
        String errorMsg = "错误码: " + response.code();
        try {
            if (response.errorBody() != null) {
                errorMsg += "\n" + response.errorBody().string();
            }
        } catch (IOException e) {
            errorMsg += "（无法解析错误详情）";
        }
        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
    }

    private void showNetworkError(String message) {
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText("网络连接错误: " + message);
    }
}
