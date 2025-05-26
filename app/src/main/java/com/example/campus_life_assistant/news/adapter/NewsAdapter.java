package com.example.campus_life_assistant.news.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.news.model.NewsItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;
    private OnNewsItemClickListener listener;
    public Set<String> likedNewsTitles = new HashSet<>();

    public interface OnNewsItemClickListener {
        void onNewsItemClick(NewsItem newsItem);
    }

    public void setOnNewsItemClickListener(OnNewsItemClickListener listener) {
        this.listener = listener;
    }

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    public void setNewsLikedStatus(String newsTitle, boolean isLiked) {
        if (isLiked) {
            likedNewsTitles.add(newsTitle);
        } else {
            likedNewsTitles.remove(newsTitle);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);
        holder.tvTitle.setText(newsItem.getTitle());
        holder.tvPublisherTime.setText("发布者：" + newsItem.getPublisher() + " " + newsItem.getPublishTime());
        holder.tvViewsLikes.setText(newsItem.getViews() + " " + newsItem.getLikes());

        if (likedNewsTitles.contains(newsItem.getTitle())) {
            holder.ivLike.setImageResource(R.drawable.ic_favorite_filled_red);
        } else {
            holder.ivLike.setImageResource(R.drawable.ic_favorite_border);
        }

        // Load image using Glide (from local resources)
        int imageResource = holder.itemView.getContext().getResources().getIdentifier(
            newsItem.getImageUrl(), "drawable", holder.itemView.getContext().getPackageName());

        if (imageResource != 0) {
            Glide.with(holder.itemView.getContext())
                 .load(imageResource)
                 .placeholder(R.drawable.ic_launcher_background) // Optional: Placeholder image while loading
                 .error(R.drawable.ic_launcher_background) // Optional: Image to show if loading fails
                 .into(holder.ivNewsImage);
        } else {
            // Handle case where resource is not found, e.g., set a default image
            holder.ivNewsImage.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                android.content.Intent intent = new android.content.Intent(v.getContext(), com.example.campus_life_assistant.news.activity.NewsDetailActivity.class);
                intent.putExtra("newsItem", newsItem);
                intent.putExtra("isLiked", likedNewsTitles.contains(newsItem.getTitle()));
                v.getContext().startActivity(intent);
            }
        });

        holder.ivLike.setOnClickListener(v -> {
            int currentLikes = newsItem.getLikes();
            String newsTitle = newsItem.getTitle();

            if (likedNewsTitles.contains(newsTitle)) {
                newsItem.setLikes(currentLikes - 1);
                likedNewsTitles.remove(newsTitle);
                holder.ivLike.setImageResource(R.drawable.ic_favorite_border);
                Toast.makeText(v.getContext(), "取消点赞", Toast.LENGTH_SHORT).show();
            } else {
                newsItem.setLikes(currentLikes + 1);
                likedNewsTitles.add(newsTitle);
                holder.ivLike.setImageResource(R.drawable.ic_favorite_filled_red);
                Toast.makeText(v.getContext(), "点赞 +1", Toast.LENGTH_SHORT).show();
            }
            holder.tvViewsLikes.setText(newsItem.getViews() + " " + newsItem.getLikes());
        });

        holder.ivShare.setOnClickListener(v -> {
            android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = newsItem.getTitle() + "\n\n" + "在此处添加新闻链接或摘要";
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
            v.getContext().startActivity(android.content.Intent.createChooser(shareIntent, "分享新闻"));
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvPublisherTime;
        TextView tvViewsLikes;
        ImageView ivNewsImage;
        ImageView ivLike;
        ImageView ivShare;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_news_title);
            tvPublisherTime = itemView.findViewById(R.id.tv_news_publisher_time);
            tvViewsLikes = itemView.findViewById(R.id.tv_news_views_likes);
            ivNewsImage = itemView.findViewById(R.id.iv_news_image);
            ivLike = itemView.findViewById(R.id.iv_news_like);
            ivShare = itemView.findViewById(R.id.iv_news_share);
        }
    }
} 