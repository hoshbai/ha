/*
package com.example.campus_life_assistant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> books;

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);

        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.categoryTextView.setText(book.getCategory());
        holder.ratingTextView.setText(String.format("%.1f", book.getRating()));

        // For demo purposes, we're not loading actual images
        // In a real app, you would use Glide or Picasso to load images
        // Glide.with(context).load(book.getImageUrl()).into(holder.bookImageView);

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "查看《" + book.getTitle() + "》详情", Toast.LENGTH_SHORT).show();
            // TODO: 跳转到图书详情页面
        });

        holder.bookmarkButton.setOnClickListener(v -> {
            book.setWishlisted(!book.isWishlisted());
            holder.bookmarkButton.setImageResource(book.isWishlisted() ?
                    android.R.drawable.btn_star_big_on :
                    android.R.drawable.btn_star_big_off);

            String message = book.isWishlisted() ?
                    "已添加《" + book.getTitle() + "》到书单" :
                    "已从书单中移除《" + book.getTitle() + "》";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImageView;
        TextView titleTextView;
        TextView authorTextView;
        TextView categoryTextView;
        TextView ratingTextView;
        ImageButton bookmarkButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            bookmarkButton = itemView.findViewById(R.id.bookmarkButton);
        }
    }
}*/
package com.example.campus_life_assistant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> books;

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder holder, final int position) {
        final Book book = books.get(position);

        // 设置书籍基本信息
        if (book.getTitle() != null) {
            holder.titleTextView.setText(book.getTitle());
        }
        holder.authorTextView.setText("作者：" + book.getAuthor());
        holder.categoryTextView.setText("分类：" + book.getCategory());
        holder.ratingTextView.setText("评分：" + String.format("%.1f", book.getRating()));

        // 显示或隐藏状态标签
        if (book.getStatus() != null && !book.getStatus().isEmpty()) {
            holder.statusTextView.setVisibility(View.VISIBLE);
            holder.statusTextView.setText(book.getStatus());
        } else {
            holder.statusTextView.setVisibility(View.GONE);
        }

        // 加载封面图片
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Glide.with(context).load(book.getImageUrl()).into(holder.bookImageView);
        } else {
            holder.bookImageView.setImageResource(android.R.drawable.ic_menu_gallery); // 默认图标
        }

        // 设置收藏状态
        holder.bookmarkButton.setImageResource(book.isWishlisted()
                ? android.R.drawable.btn_star_big_on
                : android.R.drawable.btn_star_big_off);

        // 收藏按钮点击事件
        holder.bookmarkButton.setOnClickListener(v -> {
            book.setWishlisted(!book.isWishlisted());
            holder.bookmarkButton.setImageResource(book.isWishlisted()
                    ? android.R.drawable.btn_star_big_on
                    : android.R.drawable.btn_star_big_off);

            Toast.makeText(context,
                    book.isWishlisted() ?
                            "已添加《" + book.getTitle() + "》到书单" :
                            "已从书单中移除《" + book.getTitle() + "》",
                    Toast.LENGTH_SHORT).show();
        });

        // 设置借阅状态
        holder.borrowButton.setImageResource(book.isBorrowed()
                ? android.R.drawable.ic_menu_myplaces
                : android.R.drawable.ic_menu_my_calendar);

        // 借阅按钮点击事件
        holder.borrowButton.setOnClickListener(v -> {
            book.setBorrowed(!book.isBorrowed());
            holder.borrowButton.setImageResource(book.isBorrowed()
                    ? android.R.drawable.ic_menu_myplaces
                    : android.R.drawable.ic_menu_my_calendar);

            Toast.makeText(context,
                    book.isBorrowed() ?
                            "您已借阅《" + book.getTitle() + "》" :
                            "您已归还《" + book.getTitle() + "》",
                    Toast.LENGTH_SHORT).show();
        });

        // 整个 item 的点击事件
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "查看《" + book.getTitle() + "》详情", Toast.LENGTH_SHORT).show();
            // TODO: 跳转到图书详情页
        });

        // 点击修改作者名
        holder.authorTextView.setOnClickListener(v -> {
            book.setAuthor("匿名");
            holder.authorTextView.setText("作者：" + book.getAuthor());
            Toast.makeText(context, "已将作者改为：“匿名”", Toast.LENGTH_SHORT).show();
        });

        // 点击切换分类
        holder.categoryTextView.setOnClickListener(v -> {
            String newCategory = book.getCategory().equals("小说") ? "技术" : "小说";
            book.setCategory(newCategory);
            holder.categoryTextView.setText("分类：" + book.getCategory());
            Toast.makeText(context, "分类已更新", Toast.LENGTH_SHORT).show();
        });

        // 点击修改封面图（模拟）
        holder.bookImageView.setOnClickListener(v -> {
            String newImageUrl = "https://picsum.photos/200/300?random=" + System.currentTimeMillis();
            book.setImageUrl(newImageUrl);
            Glide.with(context).load(book.getImageUrl()).into(holder.bookImageView);
            Toast.makeText(context, "封面图已更换", Toast.LENGTH_SHORT).show();
        });

        // 点击修改状态
        holder.statusTextView.setOnClickListener(v -> {
            String newStatus = book.getStatus() == null || book.getStatus().isEmpty() ? "热门推荐" : "";
            book.setStatus(newStatus);
            if (newStatus.isEmpty()) {
                holder.statusTextView.setVisibility(View.GONE);
            } else {
                holder.statusTextView.setVisibility(View.VISIBLE);
                holder.statusTextView.setText(book.getStatus());
            }
            Toast.makeText(context, "状态已更新", Toast.LENGTH_SHORT).show();
        });

        // 点击修改评分
        holder.ratingTextView.setOnClickListener(v -> {
            float newRating = (float) (Math.random() * 5); // 随机生成评分
            book.setRating(newRating);
            holder.ratingTextView.setText("评分：" + String.format("%.1f", book.getRating()));
            Toast.makeText(context, "评分已更新为：" + String.format("%.1f", newRating), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImageView;
        TextView titleTextView;
        TextView authorTextView;
        TextView categoryTextView;
        TextView ratingTextView;
        TextView statusTextView; // 新增状态 TextView
        ImageButton bookmarkButton;
        ImageButton borrowButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView); // 初始化状态 TextView
            bookmarkButton = itemView.findViewById(R.id.bookmarkButton);
            borrowButton = itemView.findViewById(R.id.borrowButton);
        }
    }
}
