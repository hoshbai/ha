package com.example.campus_life_assistant.campuscard.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.campuscard.model.CardTransaction;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.math.BigDecimal;

public class ConsumptionRecordAdapter extends RecyclerView.Adapter<ConsumptionRecordAdapter.ViewHolder> {

    private List<CardTransaction> transactionList;

    public ConsumptionRecordAdapter(List<CardTransaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consumption_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardTransaction transaction = transactionList.get(position);

        // 显示交易类型或描述
        holder.tvDescription.setText(transaction.getDescription());

        // 显示交易金额，根据金额正负设置颜色
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
            holder.tvAmount.setText(String.format(Locale.getDefault(), "+%.2f元", transaction.getAmount()));
            holder.tvAmount.setTextColor(Color.GREEN); // 收入显示绿色
        } else {
            holder.tvAmount.setText(String.format(Locale.getDefault(), "%.2f元", transaction.getAmount())); // 消费金额已经是负数
            holder.tvAmount.setTextColor(Color.RED); // 支出显示红色
        }

        // 显示交易时间
        // 注意：Timestamp 需要转换为合适的日期时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        holder.tvTime.setText(sdf.format(transaction.getTransactionTime()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;
        TextView tvAmount;
        TextView tvTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_record_description);
            tvAmount = itemView.findViewById(R.id.tv_record_amount);
            tvTime = itemView.findViewById(R.id.tv_record_time);
        }
    }
} 