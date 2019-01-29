package com.agiledev.agiledevapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class RegisterFragment extends Fragment implements View.OnTouchListener {

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_register, container, false);

        RelativeLayout layout = v.findViewById(R.id.registerlayout);
        layout.setOnTouchListener(this);

        return v;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideKeyboard.HideKeyboard(getActivity());
        return true;
    }
}
