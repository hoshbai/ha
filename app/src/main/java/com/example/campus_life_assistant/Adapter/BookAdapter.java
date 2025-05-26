package com.example.campus_life_assistant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.model.Book;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private static final String BASE_URL = "http://10.0.2.2:8081/";
    private Context context;
    private List<Book> books;
    private OnItemClickListener itemClickListener;
    private OnFavoriteClickListener favoriteClickListener;

    public interface OnItemClickListener {
        void onItemClick(int bookId);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Book book, int position);
    }

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteClickListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);

        holder.bind(book, position);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateData(List<Book> newBooks) {
        books.clear();
        books.addAll(newBooks);
        notifyDataSetChanged();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImageView;
        TextView titleTextView, authorTextView, categoryTextView, ratingTextView;
        ImageView ivFavorite;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            ivFavorite = itemView.findViewById(R.id.bookmarkButton);
        }

        void bind(Book book, int position) {
            // 加载内容
            titleTextView.setText(book.getBookName());
            authorTextView.setText(book.getAuthor());
            categoryTextView.setText(book.getCategoryName());
            ratingTextView.setText(String.format("%.1f", book.getPrice()));

            // 设置收藏状态
            ivFavorite.setImageResource(book.isFavorite() ?
                    R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);

            // 封面图片
            Glide.with(context)
                    .load(BASE_URL + "images/" + book.getImgUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_delete)
                    .into(bookImageView);

            // 点击事件
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(book.getId());
                }
            });

            // 收藏点击
            ivFavorite.setOnClickListener(v -> {
                if (favoriteClickListener != null) {
                    favoriteClickListener.onFavoriteClick(book, position);
                }
            });
        }
    }
}
