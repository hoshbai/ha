package com.example.campus_life_assistant.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
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

        // 点击事件：调用导航接口（预留）
        holder.itemView.setOnClickListener(v -> {
            // TODO: 调用高德地图导航 API
            Toast.makeText(v.getContext(), "导航至：" + place.getName(), Toast.LENGTH_SHORT).show();
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
}