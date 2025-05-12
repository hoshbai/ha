package com.example.campus_life_assistant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.model.LibraryNotification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LibraryNotificationAdapter extends RecyclerView.Adapter<LibraryNotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<LibraryNotification> notifications;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());

    public LibraryNotificationAdapter(Context context, List<LibraryNotification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_library_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        LibraryNotification notification = notifications.get(position);

        holder.titleTextView.setText(notification.getTitle());
        holder.contentTextView.setText(notification.getContent());
        holder.timeTextView.setText(dateFormat.format(new Date(notification.getTimestamp())));

        // Set icon and background based on notification type
        switch (notification.getType()) {
            case LibraryNotification.TYPE_DUE_SOON:
                holder.iconImageView.setImageResource(android.R.drawable.ic_dialog_info);
                holder.iconBackground.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
                break;
            case LibraryNotification.TYPE_OVERDUE:
                holder.iconImageView.setImageResource(android.R.drawable.ic_dialog_alert);
                holder.iconBackground.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
                break;
            case LibraryNotification.TYPE_ANNOUNCEMENT:
                holder.iconImageView.setImageResource(android.R.drawable.ic_dialog_email);
                holder.iconBackground.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
                break;
            case LibraryNotification.TYPE_RECOMMENDATION:
                holder.iconImageView.setImageResource(android.R.drawable.ic_dialog_dialer);
                holder.iconBackground.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_orange_light));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        View iconBackground;
        ImageView iconImageView;
        TextView titleTextView;
        TextView contentTextView;
        TextView timeTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            iconBackground = itemView.findViewById(R.id.iconBackground);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}
