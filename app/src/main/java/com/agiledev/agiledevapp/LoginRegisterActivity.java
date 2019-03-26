package com.agiledev.agiledevapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.stetho.Stetho;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LoginRegisterActivity extends AppCompatActivity {

    //TODO: Hash passwords with SHA-256.

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ArrayList<String> usernameList = new ArrayList<>();
    public static ArrayList<User> userList = new ArrayList<>();
    private Activity activity;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        boolean isLoggedIn = sharedPref.getBoolean(getString(R.string.prefs_loggedin_boolean),false);

        if (isLoggedIn) {
            goToMain();
        }

        populateUsers(db);

        setContentView(R.layout.activity_login_register);

        Stetho.initializeWithDefaults(this);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        activity = this;
        LinearLayout layout = findViewById(R.id.layoutLoginRegister);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CloseKeyboard.hideKeyboard(activity);
                return true;
            }
        });

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "Login");
        adapter.addFragment(new RegisterFragment(), "Register");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    protected void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public static synchronized void populateUsers(FirebaseFirestore db) {
        db.collection("UserDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Log.e("Found Username", document.getId());
                    Log.e("Found Password", document.getData().get("password").toString());
                    User user = new User();
                    user.setUsername(document.getId());
                    user.setPassword(document.getData().get("password").toString());
                    userList.add(user);
                    Log.e("Saved User", "Username: " + user.getUsername() + " | Password: " + user.getPassword());
                    Log.e("-",":-");
                }
            }
        });
    }

    public static void logIn(String username, Context mContext) {
        SharedPreferences sharedPref = mContext.getSharedPreferences("com.agiledev.agiledevapp.sharedprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean("loggedIn", true);
        editor.putString("loggedInUsername", username);
        editor.apply();



        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

    public static boolean usernameFound(String username)
    {
        for (User u : userList) {
            if (u.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public static class User {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
