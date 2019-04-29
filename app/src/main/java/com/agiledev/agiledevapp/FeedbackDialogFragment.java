package com.agiledev.agiledevapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.PendingIntent.getActivity;

public class FeedbackDialogFragment extends DialogFragment
{
    private static final String TAG = "FeedbackDialogFragment";

    //wigets
    private Button mActionSubmit, mActionCancel;
    public EditText mName, mEmail, mMessage;


    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialogfrag_feedback, container, false);

        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

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
                mName.setText("");
                mMessage.setText("");
                mEmail.setText("");
            }
        });

        mActionSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String username = sharedPref.getString(getString(R.string.prefs_loggedin_username), null);
                final String messageS = mMessage.getText().toString();

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("UserDetails").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc != null && doc.exists()) {
                                final String email = doc.get("email").toString();

                                db.collection("Feedback").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        HashMap<String, Object> feedbackIssuesMap = new HashMap<>();
                                        HashMap<String, ArrayList<String>> feedbackMap = new HashMap<>();
                                        ArrayList<String> feedbackMessages = new ArrayList<>();

                                        if (documentSnapshot.exists()) {
                                            feedbackIssuesMap = (HashMap<String, Object>)documentSnapshot.getData();
                                            feedbackMap = (HashMap<String, ArrayList<String>>) feedbackIssuesMap.get("Feedback");
                                            if (feedbackMap.get("Issues") != null) {
                                                feedbackMessages = feedbackMap.get("Issues");
                                            }
                                        }

                                        feedbackMessages.add(messageS);
                                        feedbackMap.put("Issues", feedbackMessages);
                                        feedbackIssuesMap.put("Feedback", feedbackMap);

                                        db.collection("Feedback").document(email).set(feedbackIssuesMap);

                                        FeedbackDialogFragment dialog = FeedbackDialogFragment.this;
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        return view;
    }
}