package com.agiledev.agiledevapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import static com.agiledev.agiledevapp.LoginRegisterActivity.logIn;
import static com.agiledev.agiledevapp.LoginRegisterActivity.usernameFound;

public class LoginFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private EditText txtUsername, txtPassword;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        txtUsername = v.findViewById(R.id.txtUsername);
        txtPassword = v.findViewById(R.id.txtPassword);

        LinearLayout layout = v.findViewById(R.id.layoutLogin);
        layout.setOnTouchListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (txtUsername.getText() == null || txtUsername.getText().toString().trim().equals("")) {
                    Log.e("Invalid Field", "The username field is empty!");
                    SimpleDialog.create(DialogOption.OkOnlyDismiss, view.getContext(), "Invalid Username", "The username text field was left empty!").show();
                } else if (txtPassword.getText() == null || txtPassword.getText().toString().trim().equals("")) {
                    Log.e("Invalid Field", "The password field is empty!");
                    SimpleDialog.create(DialogOption.OkOnlyDismiss, view.getContext(), "Invalid Password", "The password text field was left empty!").show();
                } else {
                    if (!usernameFound(txtUsername.getText().toString().trim())) {
                        SimpleDialog.create(DialogOption.OkOnlyDismiss, view.getContext(), "Invalid Username", "The entered username was not found!").show();
                    } else {
                        if (passwordMatchesUsername()) {
                            logIn(txtUsername.getText().toString().trim(), getContext());
                        } else {
                            SimpleDialog.create(DialogOption.OkOnlyDismiss, view.getContext(), "Invalid Password", "The password you entered was incorrect!").show();
                        }
                    }
                }
                break;
        }
    }

    private boolean passwordMatchesUsername() {
        boolean valid = false;
        for (LoginRegisterActivity.User u : LoginRegisterActivity.userList) {
            String hashedPass = RegisterFragment.hash(txtPassword.getText().toString());
            if (u.getUsername().equals(txtUsername.getText().toString().trim()) && u.getPassword().equals(hashedPass)) {
                valid = true;
            }
        }
        return valid;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CloseKeyboard.hideKeyboard(getActivity());
        return true;
    }
}