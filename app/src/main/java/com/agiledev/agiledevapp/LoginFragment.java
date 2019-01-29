package com.agiledev.agiledevapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private EditText txtUsername;
    private MobileServiceClient mClient;
    private MobileServiceTable<UserDetails> mUserDetailsTable;
    private MobileServiceList<UserDetails> results;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        txtUsername = v.findViewById(R.id.txtUsername);

        RelativeLayout layout = v.findViewById(R.id.layoutLogin);
        layout.setOnTouchListener(this);

        mClient = AzureServiceAdapter.getInstance().getClient();
        mUserDetailsTable = mClient.getTable("UserDetails", UserDetails.class);



        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:

                isUsernameValid();

//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
        }
    }

    private void isUsernameValid()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.e("Test1", "Reached before results.");
                    results = mUserDetailsTable.where().field("username").eq(txtUsername.getText().toString()).execute().get(30, TimeUnit.SECONDS);
                    Log.e("Test2", "Reached after results.");
                    if (results == null || results.size() <= 0) {
                        AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                        ad.setTitle("Username Available!");
                        ad.setMessage("This username is available!");
                        ad.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Username Unavailable").setMessage("This username is already in use!").create();
                        AlertDialog dialog = builder.show();
                    }
                }
                catch(Exception e)
                {
                    Log.e("Exception", e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CloseKeyboard.hideKeyboard(getActivity());
        return true;
    }
}