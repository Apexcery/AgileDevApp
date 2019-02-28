package com.agiledev.agiledevapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by s6104158 on 07/02/19.
 */

public class HomeFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager viewPager = myView.findViewById(R.id.viewPager);
        TabLayout tabLayout = myView.findViewById(R.id.tabLayout);
        //TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        TabAdapter Adapter = new TabAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager());
        Adapter.addFragment(new trendingMovies(), "Movies");
        Adapter.addFragment(new trendingTvshows(), "Tv Shows");
        viewPager.setAdapter(Adapter);
        tabLayout.setupWithViewPager(viewPager);

        if (getActivity().getTitle() != getString(R.string.Trending_name))
        {
            getActivity().setTitle(R.string.Trending_name);
        }

        return myView;
    }
}
