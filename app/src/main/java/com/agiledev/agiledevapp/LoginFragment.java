package com.agiledev.agiledevapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText txtUsername;
    private MobileServiceClient mClient;
    private MobileServiceTable<UserDetails> mUserDetailsTable;
    private MobileServiceList<UserDetails> results;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        txtUsername = view.findViewById(R.id.txtUsername);

        mClient = AzureServiceAdapter.getInstance().getClient();
        mUserDetailsTable = mClient.getTable("UserDetails", UserDetails.class);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:

                isUsernameValid();

//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
                break;
        }
    }

    private void isUsernameValid()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    results = mUserDetailsTable.where().field("username").eq(txtUsername.getText().toString()).execute().get(10, TimeUnit.SECONDS);
                    if (results == null || results.size() <= 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Username Available").setMessage("This username is available to use!").create();
                        AlertDialog dialog = builder.show();
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
}