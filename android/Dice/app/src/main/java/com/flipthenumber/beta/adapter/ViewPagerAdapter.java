package com.flipthenumber.beta.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.flipthenumber.beta.ui.fragments.AllUserFragment;
import com.flipthenumber.beta.ui.fragments.FriendsFragment;
import com.flipthenumber.beta.ui.fragments.InvitesFragment;
import com.flipthenumber.beta.ui.fragments.RequestFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new AllUserFragment();
        }
        else if (position == 1)
        {
            fragment = new FriendsFragment();
        }
        else if (position == 2)
        {
            fragment = new RequestFragment();
        }
        else if (position == 3)
        {
            fragment = new InvitesFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "All User";
        }
        else if (position == 1)
        {
            title = "Friends";
        }
        else if (position == 2)
        {
            title = "Request";
        }
        else if (position == 3)
        {
            title = "Invites";
        }
        return title;
    }
}
