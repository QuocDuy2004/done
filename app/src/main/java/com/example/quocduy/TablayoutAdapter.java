package com.example.quocduy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.quocduy.tab_layout.ChatFragment;
import com.example.quocduy.tab_layout.HomeFragment;
import com.example.quocduy.tab_layout.ShopFragment;

public class TablayoutAdapter extends FragmentStateAdapter{
    private String myString;
    public TablayoutAdapter(@NonNull FragmentActivity fragmentActivity){

        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position){
        switch (position){
            case 0:
                Fragment fragment1 = new HomeFragment();
                fragment1.setArguments(createBundle());
                return fragment1;
            case 1:
                Fragment fragment2 = new ShopFragment();
                fragment2.setArguments(createBundle());
                return fragment2;
            case 2:
                Fragment fragment3 = new ChatFragment();
                fragment3.setArguments(createBundle());
                return fragment3;
            default:
                Fragment fragment4 = new HomeFragment();
                fragment4.setArguments(createBundle());
                return fragment4;
        }
    }

    private Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("userObject",myString);
        return bundle;
    }

    public void setMyString(String myString){
        this.myString = myString;
    }

    @Override
    public int getItemCount(){
        return 3;
    }
}
