package com.agiledev.agiledevapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//public class SettingsFragment extends android.support.v4.app.Fragment
//{
//
//    View myView;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        myView = inflater.inflate(R.layout.fragment_help, container, false);
//      //  PreferenceFragment.addPreferencesFromResource(R.xml.preferences);
//        return myView;
//    }

// extends PreferenceFragment
public class SettingsFragment extends PreferenceFragmentCompat
{

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

//    @Override
//   public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preferences);
//
//
//    }
    /*
    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String stringValue  = o.toString();
            if(preference instanceof ListPreference)
            {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                //set the summary to reflect to the new value
                preference.setSummary(index >= 0
                        ? listPreference.getEntries()[index]
                        : null);
            }
            return false;
        }
    };
*/

    @Override
    public void onCreatePreferences(Bundle bundle, String s)
    {
    //      setPreferencesFromResource(R.xml.preferences, s);
            addPreferencesFromResource(R.xml.preferences);


        sharedPref = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();



        final android.support.v7.preference.EditTextPreference userNamePreference = (android.support.v7.preference.EditTextPreference) findPreference("key_changeUsername");
        final android.support.v7.preference.EditTextPreference userEmailPreference = (android.support.v7.preference.EditTextPreference) findPreference("key_changeEmail");
        final android.support.v7.preference.EditTextPreference userPasswordPreference = (android.support.v7.preference.EditTextPreference) findPreference("key_changePassword");

        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("key_checkbox1");
        final CheckBoxPreference checkboxPref2 = (CheckBoxPreference) getPreferenceManager().findPreference("key_checkbox_newEpisode");
        final CheckBoxPreference checkboxPref3 = (CheckBoxPreference) getPreferenceManager().findPreference("key_checkbox_trailerN");


        //default text of change username pop up settings is the current username
      //  userNamePreference.setText(getString(R.string.empty_string, sharedPref.getString(getString(R.string.prefs_loggedin_username),"Default value")));
//        String newUsername = getString(R.string.empty_string, sharedPref.getString(getString(R.string.prefs_loggedin_username),"Default value"));
//        userNamePreference.setDefaultValue(userNamePreference);
//        editor.remove("loggedInUsername");
//        editor.putString("loggedInUsername", newUsername);
//        editor.apply();

         String oldUsername = getString(R.string.empty_string, sharedPref.getString(getString(R.string.prefs_loggedin_username),"Default value")); //jj
        userNamePreference.setDefaultValue(oldUsername); // jj initially

        String newUsername = userNamePreference.getText(); // whatever is in the preference after you click submit

        editor.remove("loggedInUsername");
        editor.putString("loggedInUsername", newUsername); // in shared preferences, replace old username with the new one
        editor.apply();
                             //find the old user
        replaceDetails(db, oldUsername, newUsername, userEmailPreference); // find user in db and replace details

        //logged in as : 'new username' [only changes it after clicking on settings the second time]

        NavigationView navigationView = this.getActivity().findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.loggedInUser);
        textView.setText(getString(R.string.nav_loggedin_as, sharedPref.getString(getString(R.string.prefs_loggedin_username),"Error, user not found!")));

        //default value = current logged in as: "this"
   //     userNamePreference.setDefaultValue(getString(R.string.empty_string, sharedPref.getString(getString(R.string.prefs_loggedin_username),"Default value")));
      //  userNamePreference.setDefaultValue(userNamePreference);
     //   userNamePreference.setDefaultValue(newUsername);
   //    pullInformation(db);

  // find where when the app starts it sets the loggedInUsername \/ as text from shared preferences and call it here so you don't have to restart the app for it to change




      //  checkboxPref.setSummary(newUsername);

//        onClickPreference.setOnPreferenceClickListener(EditTextPreference f -> {
//            // do something
//            return true;
//        });

//        Map<String, Object> user = new HashMap<>();
//        user.put("dob", txtDoB.getText().toString());
//        user.put("email", txtEmail.getText().toString());
//        user.put("password", hashedPass);
//
//        RegisterFragment.registerUser(newUsername, user);  // changed registerUser to static and private -> public
////
    //   checkboxPref.setSummary(userNamePreference.getText());




//        if(checkboxPref.isChecked())
//        { // stay logged in settings checkbox
//            // stay logged in
//        } else {
//            // log user off after exiting the app
//        }
//
//        if(checkboxPref2.isChecked())
//        { // new episode notifications
//            // send the user notifications if new episode of currently watched show comes out
//        } else {
//            // do nothing, don't send any notification
//        }
//
//        if(checkboxPref3.isChecked())
//        { // new trailer notifications
//            // send the user notifications if new trailer of currently watched show comes out
//        } else {
//            // do nothing, don't send any notification
//        }

//      PreferenceFragmentCompat.setOnPreferenceChangeListener(
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object newValue) {
//        Log.e("preference", "Pending Preference value is: " + newValue);
//        return true;
//    }
//      );

    }
                                                                                                                        //CheckBoxPreference just for testing
    public static synchronized void replaceDetails(FirebaseFirestore db, final String oldUsername, final String newUsername, final android.support.v7.preference.EditTextPreference pref) {
        db.collection("UserDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                {
                    if(document.getId().equals(oldUsername)) // get the email and password of currently logged in user
                    {
                        ///////
//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        db.collection("UserDetails").document(username).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d("Success", "DocumentSnapshot successfully written!");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w("Error", "Error writing document", e);
//                            }
//                        });

                        ///


                      String oldEmail = document.getData().get("email").toString();
                      String oldPassWord = document.getData().get("password").toString();
pref.setText(oldEmail);
                        for ( int i = 0; i <   LoginRegisterActivity.userList.size(); i++)
                        {
                            if(LoginRegisterActivity.userList.get(i).getUsername().equals(oldUsername))
                            { // delete the user with old username
                                LoginRegisterActivity.userList.remove(LoginRegisterActivity.userList.get(i));
                                //delete from db
                           //     deleteUser(oldUsername, LoginRegisterActivity.userList.get(i));
                            }
                        }
                     // adding the same user with changed username if there were any
                      LoginRegisterActivity.User user = new LoginRegisterActivity.User();
                 // if user details are first changed in firebase then pull from them by using      user.setUsername(document.getId()); [if not just user newUsername
                      user.setUsername(newUsername);
                      user.setPassword(document.getData().get("password").toString());
                      user.setEmail(document.getData().get("email").toString()); ////
                      LoginRegisterActivity.userList.add(user);
                    }
//                    Log.e("Found Username", document.getId());
//                    Log.e("Found Password", document.getData().get("password").toString());
//                    Log.e("Found Email", document.getData().get("email").toString()); ////
//
//                    LoginRegisterActivity.User user = new LoginRegisterActivity.User();
//                    user.setUsername(document.getId());
//                    user.setPassword(document.getData().get("password").toString());
//                    user.setEmail(document.getData().get("email").toString()); ////
//
//                    userList.add(user);
//                    //  Log.e("Saved User", "Username: " + user.getUsername() + " | Password: " + user.getPassword());
//                    Log.e("Saved User", "Username: " + user.getUsername() + " | Password: " + user.getPassword() + " | Email: " + user.getEmail());
//                    Log.e("-",":-");
                }
            }
        });
    }

}