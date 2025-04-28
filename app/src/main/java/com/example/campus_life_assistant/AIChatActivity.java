package com.example.campus_life_assistant;


import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campus_life_assistant.Adapter.ChatAdapter;
import com.example.campus_life_assistant.Adapter.MessageAdapter;
import com.example.campus_life_assistant.ViewModel.ChatViewModel;
import com.example.campus_life_assistant.entry.Message;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AIChatActivity extends AppCompatActivity {

    private ChatAdapter adapter;
    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aichat);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextInputEditText inputText = findViewById(R.id.inputText);
        MaterialButton sendButton = findViewById(R.id.sendButton);

        // Initialize RecyclerView and Adapter
        List<Message> messages = new ArrayList<>();
        MessageAdapter adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        // Observe ViewModel response
        viewModel.getResponse().observe(this, response -> {
            if (response != null) {
                messages.add(new Message(response, false)); // AI 消息
                adapter.notifyItemInserted(messages.size() - 1);
                recyclerView.scrollToPosition(messages.size() - 1); // 滚动到底部
            }
        });

        // Handle Send Button Click
        sendButton.setOnClickListener(v -> {
            String message = inputText.getText().toString().trim();
            if (!message.isEmpty()) {
                messages.add(new Message(message, true)); // 用户消息
                adapter.notifyItemInserted(messages.size() - 1);
                recyclerView.scrollToPosition(messages.size() - 1); // 滚动到底部

                viewModel.sendMessage(message); // 发送消息
                inputText.setText(""); // 清空输入框
            }
        });
    }
}