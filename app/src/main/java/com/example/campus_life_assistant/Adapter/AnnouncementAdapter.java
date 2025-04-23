package com.example.campus_life_assistant.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

        // 设置置顶标签的可见性
        holder.tvPinned.setVisibility(announcement.isPinned() ? View.VISIBLE : View.GONE);

        // 设置背景颜色
        if (announcement.isPinned()) {
            holder.container.setBackgroundColor(Color.parseColor("#E3F2FD")); // 淡蓝色
        } else {
            holder.container.setBackgroundColor(Color.TRANSPARENT); // 默认透明背景
        }

        // 设置右上角菜单点击事件
        holder.ivMenu.setOnClickListener(v -> {
            // 创建 PopupMenu
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_announcement, popupMenu.getMenu());

            // 设置菜单项点击事件
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_pin) {
                    // 置顶逻辑
                    announcement.setPinned(true);

                    // 将置顶的公告移动到列表顶部
                    announcements.remove(position);
                    announcements.add(0, announcement);
                    notifyDataSetChanged();

                    Toast.makeText(holder.itemView.getContext(), "公告已置顶", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });

            // 显示菜单
            popupMenu.show();
        });
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
        TextView tvTitle, tvAuthor, tvDate, tvPinned;
        ImageView ivMenu; // 声明 ivMenu
        View container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivMenu = itemView.findViewById(R.id.ivMenu); // 初始化 ivMenu
            tvPinned = itemView.findViewById(R.id.tvPinned); // 初始化 ivMenu
            container = itemView.findViewById(R.id.container); // 初始化 ivMenu
        }
    }
}