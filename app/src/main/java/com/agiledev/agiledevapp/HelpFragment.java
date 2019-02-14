package com.agiledev.agiledevapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by s6104158 on 07/02/19.
 */

public class HelpFragment extends Fragment implements View.OnClickListener {

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.fragment_help, container, false);
        Button feedbackbutton = (Button) myView.findViewById(R.id.FeedbackButton);
        feedbackbutton.setOnClickListener(this);
        return myView;
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.FeedbackButton:
                Intent intent = new Intent(getActivity(), Feedback.class);
                startActivity(intent);
                break;
        }
    }
}
