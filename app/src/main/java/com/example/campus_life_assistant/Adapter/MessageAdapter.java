package com.example.campus_life_assistant.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.entry.Message;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_AI_THINK = 1;
    private static final int VIEW_TYPE_AI_REPLY = 2;

    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_AI_THINK) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ai_think, parent, false);
            return new AiThinkViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ai_reply, parent, false);
            return new AiReplyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(message.getContent());
        } else if (holder instanceof AiThinkViewHolder) {
            ((AiThinkViewHolder) holder).bind(message.getContent());
        } else if (holder instanceof AiReplyViewHolder) {
            ((AiReplyViewHolder) holder).bind(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.isUserMessage()) {
            return VIEW_TYPE_USER;
        } else if (message.isThink()) {
            return VIEW_TYPE_AI_THINK;
        } else {
            return VIEW_TYPE_AI_REPLY;
        }
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        public void bind(String content) {
            messageTextView.setText(content);
        }
    }

    static class AiThinkViewHolder extends RecyclerView.ViewHolder {
        TextView thinkTextView;

        public AiThinkViewHolder(@NonNull View itemView) {
            super(itemView);
            thinkTextView = itemView.findViewById(R.id.thinkTextView);
        }

        public void bind(String content) {
            thinkTextView.setText(content);
        }
    }

    static class AiReplyViewHolder extends RecyclerView.ViewHolder {
        TextView replyTextView;

        public AiReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            replyTextView = itemView.findViewById(R.id.replyTextView);
        }

        public void bind(String content) {
            replyTextView.setText(content);
        }
    }
}