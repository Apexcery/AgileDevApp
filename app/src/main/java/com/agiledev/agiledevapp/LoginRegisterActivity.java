package com.agiledev.agiledevapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

//import com.facebook.stetho.Stetho;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginRegisterActivity extends AppCompatActivity {

    private Activity activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ArrayList<String> usernameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean(getString(R.string.prefs_loggedin_boolean),false);

        if (isLoggedIn) {
            String loggedInUsername = sharedPref.getString(getString(R.string.prefs_loggedin_username), null);
            goToMain();
        }

        populateUsernames(db);

        setContentView(R.layout.activity_login_register);

//        Stetho.initializeWithDefaults(this);

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

    public static synchronized void populateUsernames(FirebaseFirestore db) {
        db.collection("UserDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Log.e("Found Username", document.getId());
                    usernameList.add(document.getId());
                }
            }
        });
    }
}
