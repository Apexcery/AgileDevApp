package com.agiledev.agiledevapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ActivityHelp extends AppCompatActivity
{
    private static final String TAG = "ActivityHelp";
    private Button mOpenDialog;
    public FeedbackDialogFragment dialog = new FeedbackDialogFragment();
    public issueDialogFragment issueDialog = new issueDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button ReportButton = findViewById(R.id.ReportIssue);
        ReportButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                issueDialog.show(getFragmentManager(), "issueDialogFragment");
            }
        });

        mOpenDialog = findViewById(R.id.FeedbackButton);
        mOpenDialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                dialog.show(getFragmentManager(), "FeedbackDialogFragment");
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

}
