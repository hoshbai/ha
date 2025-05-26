package com.example.campus_life_assistant.campuscard.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.campuscard.model.ConsumptionRecord;

import java.util.List;

public class ConsumptionRecordAdapter extends RecyclerView.Adapter<ConsumptionRecordAdapter.RecordViewHolder> {

    private List<ConsumptionRecord> recordList;

    public ConsumptionRecordAdapter(List<ConsumptionRecord> recordList) {
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consumption_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        ConsumptionRecord record = recordList.get(position);
        holder.tvDescription.setText(record.getDescription());
        holder.tvTime.setText(record.getTime());
        holder.tvAmount.setText(String.format(java.util.Locale.getDefault(), "%.2f元", record.getAmount()));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;
        TextView tvTime;
        TextView tvAmount;

        RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_record_description);
            tvTime = itemView.findViewById(R.id.tv_record_time);
            tvAmount = itemView.findViewById(R.id.tv_record_amount);
        }
    }
} 