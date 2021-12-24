package com.flipthenumber.beta.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flipthenumber.beta.adapter.ViewPagerAdapter;
import com.flipthenumber.beta.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendListActivity extends AppCompatActivity  implements View.OnClickListener {
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        init();
        initUI();
    }

    private void init(){
        ButterKnife.bind(this);

    }

    private void initUI(){
        img_back.setOnClickListener(this);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        for(int i=0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(5, 0, 5, 0);
            tab.requestLayout();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}