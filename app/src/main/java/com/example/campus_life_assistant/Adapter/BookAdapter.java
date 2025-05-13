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
}