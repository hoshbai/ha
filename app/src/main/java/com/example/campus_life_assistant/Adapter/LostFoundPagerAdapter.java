package com.example.campus_life_assistant.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.campus_life_assistant.entry.LostFoundItem;
import com.example.campus_life_assistant.fragment.LostFoundListFragment;

public class LostFoundPagerAdapter extends FragmentStateAdapter {
    
    private static final int TAB_ALL = 0;
    private static final int TAB_LOST = 1;
    private static final int TAB_FOUND = 2;
    private static final int TAB_COUNT = 3;

    public LostFoundPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 根据位置创建相应的Fragment
        switch (position) {
            case TAB_ALL:
                // 全部
                return LostFoundListFragment.newInstance(null);
            case TAB_LOST:
                // 寻物启事
                return LostFoundListFragment.newInstance(LostFoundItem.TYPE_LOST);
            case TAB_FOUND:
                // 招领启事
                return LostFoundListFragment.newInstance(LostFoundItem.TYPE_FOUND);
            default:
                return LostFoundListFragment.newInstance(null);
        }
    }

    @Override
    public int getItemCount() {
        return TAB_COUNT;
    }
} 