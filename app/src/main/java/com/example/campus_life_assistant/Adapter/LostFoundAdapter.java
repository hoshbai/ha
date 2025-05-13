package com.example.campus_life_assistant.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.entry.LostFoundItem;

import java.util.List;

public class LostFoundAdapter extends RecyclerView.Adapter<LostFoundAdapter.LostFoundViewHolder> {

    private List<LostFoundItem> itemList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LostFoundItem item);
        void onContactClick(LostFoundItem item);
        void onCompleteClick(LostFoundItem item, int position);
    }

    public LostFoundAdapter(Context context, List<LostFoundItem> itemList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LostFoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lost_found, parent, false);
        return new LostFoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LostFoundViewHolder holder, int position) {
        LostFoundItem item = itemList.get(position);
        
        // 设置标签颜色和文本
        int tagColorResId = item.getItemType() == LostFoundItem.TYPE_LOST ? 
                R.color.color_lost : R.color.color_found;
        holder.tagTextView.setBackgroundResource(tagColorResId);
        holder.tagTextView.setText(item.getItemTypeText());
        
        // 设置物品信息
        holder.titleTextView.setText(item.getTitle());
        holder.categoryTextView.setText(item.getCategory());
        
        // 格式化日期时间
        String dateTimeStr = DateFormat.format("yyyy-MM-dd HH:mm", item.getTime()).toString();
        holder.timeTextView.setText(dateTimeStr);
        
        holder.locationTextView.setText(item.getLocation());
        holder.descriptionTextView.setText(item.getDescription());
        
        // 设置联系方式
        String contactInfo = item.getContactType() + ": " + item.getContact();
        holder.contactTextView.setText(contactInfo);
        
        // 设置发布者和发布时间
        String publishInfo = "发布者: " + item.getPublisherName();
        String publishTimeStr = DateFormat.format("MM-dd HH:mm", item.getPublishTime()).toString();
        publishInfo += " | 发布时间: " + publishTimeStr;
        holder.publishInfoTextView.setText(publishInfo);
        
        // 设置按钮状态
        if (item.isCompleted()) {
            holder.completeButton.setText("已完成");
            holder.completeButton.setEnabled(false);
        } else {
            String btnText = item.getItemType() == LostFoundItem.TYPE_LOST ? "已找到" : "已归还";
            holder.completeButton.setText(btnText);
            holder.completeButton.setEnabled(true);
        }
        
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
        
        holder.contactButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContactClick(item);
            }
        });
        
        holder.completeButton.setOnClickListener(v -> {
            if (listener != null && !item.isCompleted()) {
                listener.onCompleteClick(item, holder.getAdapterPosition());
            }
        });
        
        // 设置图片（此处使用默认图片，实际应用中可使用图片加载库如Glide）
        holder.itemImageView.setImageResource(R.drawable.ic_launcher_foreground);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }
    
    // 更新数据列表
    public void updateData(List<LostFoundItem> newItemList) {
        this.itemList = newItemList;
        notifyDataSetChanged();
    }
    
    // 更新单个项目的状态
    public void updateItemStatus(int position, boolean isCompleted) {
        if (position >= 0 && position < itemList.size()) {
            itemList.get(position).setCompleted(isCompleted);
            notifyItemChanged(position);
        }
    }

    static class LostFoundViewHolder extends RecyclerView.ViewHolder {
        TextView tagTextView;
        TextView titleTextView;
        TextView categoryTextView;
        TextView timeTextView;
        TextView locationTextView;
        TextView descriptionTextView;
        TextView contactTextView;
        TextView publishInfoTextView;
        ImageView itemImageView;
        Button contactButton;
        Button completeButton;

        public LostFoundViewHolder(@NonNull View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.lost_found_tag);
            titleTextView = itemView.findViewById(R.id.lost_found_title);
            categoryTextView = itemView.findViewById(R.id.lost_found_category);
            timeTextView = itemView.findViewById(R.id.lost_found_time);
            locationTextView = itemView.findViewById(R.id.lost_found_location);
            descriptionTextView = itemView.findViewById(R.id.lost_found_description);
            contactTextView = itemView.findViewById(R.id.lost_found_contact);
            publishInfoTextView = itemView.findViewById(R.id.lost_found_publish_info);
            itemImageView = itemView.findViewById(R.id.lost_found_image);
            contactButton = itemView.findViewById(R.id.lost_found_contact_btn);
            completeButton = itemView.findViewById(R.id.lost_found_complete_btn);
        }
    }
} 