// app/src/main/java/com/example/campus_life_assistant/Adapter/LibraryNotificationAdapter.java
package com.example.campus_life_assistant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.model.LibraryNotification;

import java.util.List;

public class LibraryNotificationAdapter
        extends RecyclerView.Adapter<LibraryNotificationAdapter.ViewHolder> {

    private Context context;
    private List<LibraryNotification> notifications;

    public LibraryNotificationAdapter(
            Context context, List<LibraryNotification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_library_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {
        LibraryNotification notification = notifications.get(position);
        // Set title if needed, here using message as title
        holder.titleTextView.setText("通知");
        // Display message content
        holder.contentTextView.setText(notification.getMessage());
        // Display date/time
        holder.timeTextView.setText(notification.getDate());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView timeTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView   = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timeTextView    = itemView.findViewById(R.id.timeTextView);
        }
    }
}
