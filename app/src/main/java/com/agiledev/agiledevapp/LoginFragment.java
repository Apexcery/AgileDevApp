package com.agiledev.agiledevapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private EditText txtUsername;
    private View v;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<String> usernameList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_login, container, false);

        populateUsernames();

        Button btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        txtUsername = v.findViewById(R.id.txtUsername);

        RelativeLayout layout = v.findViewById(R.id.layoutLogin);
        layout.setOnTouchListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (txtUsername.getText() == null || txtUsername.getText().toString().equals(""))  {
                    Log.e("Empty Input", "The username textbox is empty.");
                    SimpleDialog.create(DialogOption.OkOnlyDismiss, view.getContext(), "Invalid Username", "The username text field was left empty!").show();
                } else {
                    if(!usernameFound()) {
                        SimpleDialog.create(DialogOption.OkOnlyDismiss, view.getContext(), "Invalid Username", "The entered username was not found!").show();
                    } else {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    private boolean usernameFound()
    {
        return usernameList.contains(txtUsername.getText().toString());
    }

    public synchronized void populateUsernames() {
        db.collection("UserDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    usernameList.add(document.getId());
                    Log.e("Found Username", document.getId());
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CloseKeyboard.hideKeyboard(getActivity());
        return true;
    }

}