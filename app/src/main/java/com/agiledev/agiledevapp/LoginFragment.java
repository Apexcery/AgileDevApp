package com.agiledev.agiledevapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private EditText txtUsername;
    private View v;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        txtUsername = v.findViewById(R.id.txtUsername);

        ConstraintLayout layout = v.findViewById(R.id.loginlayout);
        layout.setOnTouchListener(this);

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
                        logIn(txtUsername.getText().toString());
                    }
                }
                break;
        }
    }

    private boolean usernameFound()
    {
        return LoginRegisterActivity.usernameList.contains(txtUsername.getText().toString());
    }

    private void logIn(String username) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean(getString(R.string.prefs_loggedin_boolean), true);
        editor.putString(getString(R.string.prefs_loggedin_username), username);

        editor.apply();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CloseKeyboard.hideKeyboard(getActivity());
        return true;
    }
}