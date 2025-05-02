package com.example.campus_life_assistant.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.SchoolMapActivity;
import com.example.campus_life_assistant.entry.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<Place> places = new ArrayList<>();

    public void submitList(List<Place> newData) {
        places.clear();
        places.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place_card, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = places.get(position);
        holder.imageView.setImageResource(place.getImageResId());
        holder.nameView.setText(place.getName());
        holder.descView.setText(place.getDescription());

        // 点击事件：弹出路径选择对话框
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();

            // 获取当前位置
            double startLat = SchoolMapActivity.getCurrentLatitude();
            double startLon = SchoolMapActivity.getCurrentLongitude();
            double endLat = place.getLatitude();
            double endLon = place.getLongitude();

            // 检查是否获取到当前位置
            if (startLat == 0 && startLon == 0) {
                Toast.makeText(context, "未获取到当前位置，请稍后再试", Toast.LENGTH_SHORT).show();
                return;
            }

            // 弹出路径选择对话框
            showNavigationOptionsDialog(context, startLat, startLon, endLat, endLon);
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView, descView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_place_image);
            nameView = itemView.findViewById(R.id.tv_place_name);
            descView = itemView.findViewById(R.id.tv_place_desc);
        }
    }

    // 显示路径选择对话框（新增公交选项）
    private void showNavigationOptionsDialog(Context context, double startLat, double startLon, double endLat, double endLon) {
        String[] options = {"驾车", "步行", "骑行", "公交"};
        new AlertDialog.Builder(context)
                .setTitle("选择导航方式")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            ((SchoolMapActivity) context).startDrivingRoute(startLat, startLon, endLat, endLon);
                            break;
                        case 1:
                            ((SchoolMapActivity) context).startWalkingRoute(startLat, startLon, endLat, endLon);
                            break;
                        case 2:
                            ((SchoolMapActivity) context).startRidingRoute(startLat, startLon, endLat, endLon);
                            break;
                        case 3:
                            ((SchoolMapActivity) context).startBusRoute(startLat, startLon, endLat, endLon);
                            break;
                    }
                })
                .show();
    }
}