package com.example.campus_life_assistant.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.entry.Announcement;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private final List<Announcement> announcements;
    private final OnItemClickListener listener;

    public AnnouncementAdapter(List<Announcement> announcements, OnItemClickListener listener) {
        this.announcements = announcements;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sushe_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.tvTitle.setText(announcement.getTitle());
        holder.tvAuthor.setText("发布人：" + announcement.getAuthor());
        holder.tvDate.setText(announcement.getDate());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(announcement));
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Announcement announcement);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}