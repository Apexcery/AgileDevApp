package com.agiledev.agiledevapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityHelp extends AppCompatActivity
{
    private static final String TAG = "ActivityHelp";
    private Button mOpenDialog;
    public TextView mInputDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mOpenDialog = findViewById(R.id.FeedbackButton);

        mOpenDialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FeedbackDialogFragment dialog = new FeedbackDialogFragment();
                dialog.show(getFragmentManager(), "FeedbackDialogFragment");
            }
        });

    }

}
