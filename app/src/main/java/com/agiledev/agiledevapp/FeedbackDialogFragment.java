package com.agiledev.agiledevapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.app.PendingIntent.getActivity;

public class FeedbackDialogFragment extends DialogFragment
{
    private static final String TAG = "FeedbackDialogFragment";

    //wigets
    private Button mActionSubmit, mActionCancel;
    public EditText mName, mEmail, mMessage;

    public ThankyouDialogFragment dialog = new ThankyouDialogFragment();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialogfrag_feedback, container, false);
        mActionSubmit = view.findViewById(R.id.issueSubmitbutton);
        mActionCancel = view.findViewById(R.id.issuecancelbutton);
        mName = view.findViewById(R.id.issueNameText);
        mEmail = view.findViewById(R.id.issueEmailText);
        mMessage = view.findViewById(R.id.issuemessagetext);

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
                String nameS = mName.getText().toString();
                String emailS = mEmail.getText().toString();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[] { "s6104158@live.tees.ac.uk" });
                email.putExtra(Intent.EXTRA_SUBJECT, "Feedback by: " + nameS);
                email.putExtra(Intent.EXTRA_TEXT, messageS + "\n\n Email: " + emailS);

                email.setType("text/email");
                startActivity(Intent.createChooser(email, "Choose app to send email"));

                getDialog().dismiss();
                dialog.show(getFragmentManager(), "ThankyouDialogFragment");
            }

        });

        return view;
    }

    public boolean onTouch(View v, MotionEvent event) {
        CloseKeyboard.hideKeyboard(getActivity());
        return true;
    }

}