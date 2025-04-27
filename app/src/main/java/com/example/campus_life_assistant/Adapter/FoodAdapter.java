package com.example.campus_life_assistant.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.FoodDetailActivity;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.entry.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private List<Food> foodList;
    private boolean isRecommendation;

    public FoodAdapter(Context context, List<Food> foodList, boolean isRecommendation) {
        this.context = context;
        this.foodList = foodList;
        this.isRecommendation = isRecommendation;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isRecommendation) {
            // 推荐菜品使用横向卡片布局
            view = LayoutInflater.from(context).inflate(R.layout.item_food_recommendation, parent, false);
        } else {
            // 普通菜品使用列表项布局
            view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        }
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);

        holder.foodName.setText(food.getName());
        holder.foodDescription.setText(food.getDescription());
        holder.foodPrice.setText(String.format("¥%.2f", food.getPrice()));
        holder.foodRating.setRating(food.getRating());

        // 如果有评分，显示评分计数
        if (food.getRatingCount() > 0) {
            holder.foodRatingCount.setText(String.format("(%d)", food.getRatingCount()));
            holder.foodRatingCount.setVisibility(View.VISIBLE);
        } else {
            holder.foodRatingCount.setVisibility(View.GONE);
        }

        // 推荐标签只在常规列表中显示
        if (!isRecommendation && food.isRecommended()) {
            holder.recommendedTag.setVisibility(View.VISIBLE);
        } else {
            holder.recommendedTag.setVisibility(View.GONE);
        }

        // 设置图片（此处应使用图片加载库如Glide或Picasso）
        // Glide.with(context).load(food.getImageUrl()).into(holder.foodImage);

        // 临时使用占位图
        holder.foodImage.setImageResource(R.drawable.food_placeholder);

        // 点击事件 - 打开详情页
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodDetailActivity.class);
            intent.putExtra("food", food);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void updateData(List<Food> newFoodList) {
        this.foodList = newFoodList;
        notifyDataSetChanged();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodDescription;
        TextView foodPrice;
        RatingBar foodRating;
        TextView foodRatingCount;
        TextView recommendedTag;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodRating = itemView.findViewById(R.id.food_rating);
            foodRatingCount = itemView.findViewById(R.id.food_rating_count);
            recommendedTag = itemView.findViewById(R.id.recommended_tag);
        }
    }
}