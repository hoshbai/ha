package com.example.campus_life_assistant.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.entry.Canteen;
import com.example.campus_life_assistant.entry.Food;
import com.example.campus_life_assistant.entry.FoodComment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanteenManager {
    private static final String TAG = "CanteenManager";
    private static final String PREF_NAME = "canteen_data";
    private static final String KEY_USER_RATINGS = "user_ratings";
    private static final String KEY_USER_COMMENTS = "user_comments";

    private static CanteenManager instance;
    private Context context;
    private List<Canteen> canteens;
    private Map<String, Float> userRatings; // 用户对菜品的评分，格式：foodId -> rating
    private Map<String, List<FoodComment>> userComments; // 用户对菜品的评论，格式：foodId -> comments
    private SharedPreferences securePreferences;
    private Gson gson;

    private CanteenManager(Context context) {
        this.context = context.getApplicationContext();
        this.canteens = new ArrayList<>();
        this.gson = new Gson();
        initializeSecureStorage();
        loadUserData();
        initializeSampleData(); // 加载示例数据
    }

    public static synchronized CanteenManager getInstance(Context context) {
        if (instance == null) {
            instance = new CanteenManager(context);
        }
        return instance;
    }

    // 初始化安全存储 - 适配Android 12隐私保护
    private void initializeSecureStorage() {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            securePreferences = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "Failed to initialize secure storage", e);
            // 回退到标准SharedPreferences
            securePreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    // 加载用户数据
    private void loadUserData() {
        String ratingsJson = securePreferences.getString(KEY_USER_RATINGS, null);
        if (ratingsJson != null) {
            Type type = new TypeToken<HashMap<String, Float>>(){}.getType();
            userRatings = gson.fromJson(ratingsJson, type);
        } else {
            userRatings = new HashMap<>();
        }

        String commentsJson = securePreferences.getString(KEY_USER_COMMENTS, null);
        if (commentsJson != null) {
            Type type = new TypeToken<HashMap<String, List<FoodComment>>>(){}.getType();
            userComments = gson.fromJson(commentsJson, type);
        } else {
            userComments = new HashMap<>();
        }
    }

    // 保存用户数据
    private void saveUserData() {
        SharedPreferences.Editor editor = securePreferences.edit();
        editor.putString(KEY_USER_RATINGS, gson.toJson(userRatings));
        editor.putString(KEY_USER_COMMENTS, gson.toJson(userComments));
        editor.apply();
    }

    // 初始化示例数据
    private void initializeSampleData() {
        // 创建几个食堂
        Canteen canteen1 = new Canteen("第一食堂", "东校区", "07:00-20:00");
        canteen1.setQueueStatus(2);

        Canteen canteen2 = new Canteen("第二食堂", "西校区", "06:30-21:00");
        canteen2.setQueueStatus(4);

        Canteen canteen3 = new Canteen("教工餐厅", "中心区", "10:00-14:00, 17:00-20:00");
        canteen3.setQueueStatus(1);

        // 添加菜品到食堂1
        Food food1 = new Food("宫保鸡丁", "麻辣鲜香的经典川菜", 12.0, "荤菜", null);
        Food food2 = new Food("西红柿炒鸡蛋", "家常美味，酸甜可口", 8.0, "荤菜", null);
        Food food3 = new Food("红烧排骨", "肉质酥烂，香味浓郁", 18.0, "荤菜", null);
        Food food4 = new Food("炒青菜", "新鲜爽口，清淡健康", 6.0, "素菜", null);
        Food food5 = new Food("米饭", "新鲜蒸制", 2.0, "主食", null);
        Food food6 = new Food("馒头", "松软可口", 1.0, "主食", null);

        canteen1.addFood(food1);
        canteen1.addFood(food2);
        canteen1.addFood(food3);
        canteen1.addFood(food4);
        canteen1.addFood(food5);
        canteen1.addFood(food6);

        // 添加推荐菜品
        canteen1.addRecommendation(food1);
        canteen1.addRecommendation(food3);

        // 添加菜品到食堂2
        Food food7 = new Food("牛肉面", "汤浓肉香，面条劲道", 15.0, "面食", null);
        Food food8 = new Food("小笼包", "鲜香多汁，皮薄馅大", 10.0, "点心", null);
        Food food9 = new Food("水煮鱼", "鲜辣爽口，肉质鲜嫩", 25.0, "荤菜", null);

        canteen2.addFood(food7);
        canteen2.addFood(food8);
        canteen2.addFood(food9);

        // 添加推荐菜品
        canteen2.addRecommendation(food7);
        canteen2.addRecommendation(food9);

        // 添加到列表
        canteens.add(canteen1);
        canteens.add(canteen2);
        canteens.add(canteen3);

        // 应用已保存的用户评分和评论
        applyUserRatingsAndComments();
    }

    // 应用已保存的用户评分和评论
    private void applyUserRatingsAndComments() {
        for (Canteen canteen : canteens) {
            for (Food food : canteen.getMenu()) {
                String foodId = food.getName(); // 使用名称作为ID

                // 应用评分
                Float userRating = userRatings.get(foodId);
                if (userRating != null) {
                    food.addRating(userRating);
                }

                // 应用评论
                List<FoodComment> comments = userComments.get(foodId);
                if (comments != null) {
                    for (FoodComment comment : comments) {
                        food.addComment(comment);
                    }
                }
            }
        }
    }

    // 获取所有食堂
    public List<Canteen> getAllCanteens() {
        return canteens;
    }

    // 获取食堂菜单
    public List<Food> getCanteenMenu(String canteenName) {
        for (Canteen canteen : canteens) {
            if (canteen.getName().equals(canteenName)) {
                return canteen.getMenu();
            }
        }
        return new ArrayList<>();
    }

    // 获取食堂推荐菜品
    public List<Food> getCanteenRecommendations(String canteenName) {
        for (Canteen canteen : canteens) {
            if (canteen.getName().equals(canteenName)) {
                return canteen.getRecommendations();
            }
        }
        return new ArrayList<>();
    }

    // 添加用户评分和评论
    public void addRatingAndComment(Food food, float rating, String comment, String userNickname) {
        String foodId = food.getName();

        // 保存评分
        userRatings.put(foodId, rating);

        // 创建并保存评论
        if (comment != null && !comment.isEmpty()) {
            FoodComment foodComment = new FoodComment(userNickname, rating, comment);

            List<FoodComment> comments = userComments.get(foodId);
            if (comments == null) {
                comments = new ArrayList<>();
                userComments.put(foodId, comments);
            }
            comments.add(foodComment);

            // 更新食物对象
            food.addComment(foodComment);
        }

        // 更新食物评分
        food.addRating(rating);

        // 保存到安全存储
        saveUserData();
    }

    // 更新食堂排队状态
    public void updateQueueStatus(String canteenName, int newStatus) {
        for (Canteen canteen : canteens) {
            if (canteen.getName().equals(canteenName)) {
                canteen.setQueueStatus(newStatus);
                break;
            }
        }
    }
}