package com.example.campus_life_assistant.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.campus_life_assistant.fragment.BookCategoryFragment;

public class LibraryTabPagerAdapter extends FragmentStateAdapter {

    private static final int TAB_COUNT = 10; // Total number of tabs

    public LibraryTabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Create new BookCategoryFragment with the category position
        BookCategoryFragment fragment = new BookCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(BookCategoryFragment.ARG_CATEGORY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return TAB_COUNT;
    }
}
