package com.izhar.ism.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.izhar.ism.fragments.DeclinedTab;
import com.izhar.ism.fragments.FinishedTab;
import com.izhar.ism.fragments.PendingTab;


public class PagerAdapter extends FragmentStatePagerAdapter {
    private int tab_numbers;
    String user;
    public PagerAdapter(@NonNull FragmentManager fm, int tab_numbers, String user) {
        super(fm);
        this.tab_numbers = tab_numbers;
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PendingTab pendingTab = new PendingTab(user);
                return pendingTab;
            case 1:
                FinishedTab finishedTab = new FinishedTab(user);
                return finishedTab;
            case 2:
                DeclinedTab declinedTab = new DeclinedTab(user);
                return declinedTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tab_numbers;
    }
}
