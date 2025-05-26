package com.example.campus_life_assistant.news.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.news.model.NewsComment;

import java.util.List;

public class NewsCommentAdapter extends RecyclerView.Adapter<NewsCommentAdapter.CommentViewHolder> {

    private List<NewsComment> commentList;

    public NewsCommentAdapter(List<NewsComment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        NewsComment comment = commentList.get(position);
        holder.tvCommenterName.setText(comment.getCommenterName());
        holder.tvCommentContent.setText(comment.getContent());
        holder.tvCommentTime.setText(comment.getCommentTime());
        holder.tvCommentLikes.setText(comment.getLikes() + "赞");
        // Handle comment like icon click if needed
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommenterName;
        TextView tvCommentContent;
        TextView tvCommentTime;
        TextView tvCommentLikes;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommenterName = itemView.findViewById(R.id.tv_commenter_name);
            tvCommentContent = itemView.findViewById(R.id.tv_comment_content);
            tvCommentTime = itemView.findViewById(R.id.tv_comment_time);
            tvCommentLikes = itemView.findViewById(R.id.tv_comment_likes);
        }
    }
} 