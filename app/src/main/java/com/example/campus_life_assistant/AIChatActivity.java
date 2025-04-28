package com.example.campus_life_assistant;

import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campus_life_assistant.Adapter.MessageAdapter;
import com.example.campus_life_assistant.ViewModel.ChatViewModel;
import com.example.campus_life_assistant.entry.Message;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AIChatActivity extends AppCompatActivity {

    private ChatViewModel viewModel;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aichat);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextInputEditText inputText = findViewById(R.id.inputText);
        MaterialButton sendButton = findViewById(R.id.sendButton);

        // 初始化 RecyclerView 和 Adapter
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化 ViewModel
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        // 观察 <think> 部分
        viewModel.getThinkResponse().observe(this, think -> {
            if (!think.isEmpty()) {
                messages.add(new Message(think, false, true)); // AI 思考部分
                adapter.notifyItemInserted(messages.size() - 1);
                recyclerView.scrollToPosition(messages.size() - 1); // 滚动到底部
            }
        });

        // 观察正式回答部分
        viewModel.getFinalResponse().observe(this, finalResponse -> {
            if (!finalResponse.isEmpty()) {
                messages.add(new Message(finalResponse, false, false)); // AI 正式回答部分
                adapter.notifyItemInserted(messages.size() - 1);
                recyclerView.scrollToPosition(messages.size() - 1); // 滚动到底部
            }
        });

        // 处理发送按钮点击事件
        sendButton.setOnClickListener(v -> {
            String message = inputText.getText().toString().trim();
            if (!message.isEmpty()) {
                messages.add(new Message(message, true, false)); // 用户消息
                adapter.notifyItemInserted(messages.size() - 1);
                recyclerView.scrollToPosition(messages.size() - 1); // 滚动到底部

                viewModel.sendMessage(message); // 发送消息
                inputText.setText(""); // 清空输入框
            }
        });
    }
}