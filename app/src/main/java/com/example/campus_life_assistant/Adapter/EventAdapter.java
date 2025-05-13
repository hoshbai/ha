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
import com.example.campus_life_assistant.entry.CampusEvent;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<CampusEvent> eventList;
    private Context context;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(CampusEvent event);
        void onRegisterClick(CampusEvent event, int position);
    }

    public EventAdapter(Context context, List<CampusEvent> eventList, OnEventClickListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        CampusEvent event = eventList.get(position);
        holder.titleTextView.setText(event.getTitle());
        
        // 格式化日期时间
        String dateStr = DateFormat.format("yyyy年MM月dd日 HH:mm", event.getStartTime()).toString();
        holder.timeTextView.setText(dateStr);
        
        holder.locationTextView.setText(event.getLocation());
        holder.organizerTextView.setText(event.getOrganizer());
        holder.categoryTextView.setText(event.getCategory());
        
        // 设置按钮状态
        if (event.isRegistered()) {
            holder.registerButton.setText("已报名");
            holder.registerButton.setEnabled(false);
        } else {
            holder.registerButton.setText("报名参加");
            holder.registerButton.setEnabled(true);
        }
        
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEventClick(event);
            }
        });
        
        holder.registerButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRegisterClick(event, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList == null ? 0 : eventList.size();
    }
    
    // 更新数据列表
    public void updateData(List<CampusEvent> newEventList) {
        this.eventList = newEventList;
        notifyDataSetChanged();
    }
    
    // 更新单个项目的状态
    public void updateEventStatus(int position, boolean isRegistered) {
        if (position >= 0 && position < eventList.size()) {
            eventList.get(position).setRegistered(isRegistered);
            notifyItemChanged(position);
        }
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImageView;
        TextView titleTextView;
        TextView timeTextView;
        TextView locationTextView;
        TextView organizerTextView;
        TextView categoryTextView;
        Button registerButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImageView = itemView.findViewById(R.id.event_image);
            titleTextView = itemView.findViewById(R.id.event_title);
            timeTextView = itemView.findViewById(R.id.event_time);
            locationTextView = itemView.findViewById(R.id.event_location);
            organizerTextView = itemView.findViewById(R.id.event_organizer);
            categoryTextView = itemView.findViewById(R.id.event_category);
            registerButton = itemView.findViewById(R.id.event_register_button);
        }
    }
} 