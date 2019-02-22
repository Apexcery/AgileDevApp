package com.agiledev.agiledevapp;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.app.PendingIntent.getActivity;

public class FeedbackDialogFragment extends DialogFragment
{
    private static final String TAG = "FeedbackDialogFragment";

    //wigets
    private Button mActionSubmit, mActionCancel;
    public EditText mName, mEmail, mSubject, mMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfrag_feedback, container, false);
        mActionSubmit = view.findViewById(R.id.Submitbutton);
        mActionCancel = view.findViewById(R.id.cancelbutton);
        mName = view.findViewById(R.id.NameText);
        mEmail = view.findViewById(R.id.EmailText);
        mSubject = view.findViewById(R.id.subjecttext);
        mMessage = view.findViewById(R.id.messagetext);

        mActionCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getDialog().dismiss();
            }
        });

        mActionSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String messageS = mMessage.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[] { "s6104158@live.tees.ac.uk" });
                email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                email.putExtra(Intent.EXTRA_TEXT, messageS);

                email.setType("text/email");
                startActivity(Intent.createChooser(email, "Choose app to send email"));


                getDialog().dismiss();
            }

        });


        return view;
    }
}