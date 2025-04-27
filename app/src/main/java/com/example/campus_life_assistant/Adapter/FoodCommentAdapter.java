package com.example.campus_life_assistant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.entry.FoodComment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FoodCommentAdapter extends RecyclerView.Adapter<FoodCommentAdapter.CommentViewHolder> {

    private Context context;
    private List<FoodComment> comments;
    private SimpleDateFormat dateFormat;

    public FoodCommentAdapter(Context context, List<FoodComment> comments) {
        this.context = context;
        this.comments = comments;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        FoodComment comment = comments.get(position);

        holder.tvUserName.setText(comment.getUserNickname());
        holder.tvCommentDate.setText(dateFormat.format(comment.getCommentDate()));
        holder.rbUserRating.setRating(comment.getRating());

        // 设置评论内容（如果有）
        if (comment.getComment() != null && !comment.getComment().isEmpty()) {
            holder.tvComment.setText(comment.getComment());
            holder.tvComment.setVisibility(View.VISIBLE);
        } else {
            holder.tvComment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void updateData(List<FoodComment> newComments) {
        this.comments = newComments;
        notifyDataSetChanged();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvCommentDate;
        RatingBar rbUserRating;
        TextView tvComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvCommentDate = itemView.findViewById(R.id.tv_comment_date);
            rbUserRating = itemView.findViewById(R.id.rb_user_rating);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }
}