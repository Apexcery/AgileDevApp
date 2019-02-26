package com.agiledev.agiledevapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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

        //TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        TabAdapter Adapter = new TabAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager());
        Adapter.addFragment(new LoginFragment(), "Login");
        Adapter.addFragment(new RegisterFragment(), "Register");
        //viewPager.setAdapter(adapter);
        //tabLayout.setupWithViewPager(viewPager);

        return myView;
    }

}
