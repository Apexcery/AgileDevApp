package com.agiledev.agiledevapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.annotation.NonNull;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.Preference;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends PreferenceFragmentCompat
{
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String currentUserName;
    Map<String, String[]> currentUserDetails = new HashMap<String, String[]>();
    public static boolean stayLoggedIn = true;

    @Override
    public void onCreatePreferences(Bundle bundle, String s)
    {
        addPreferencesFromResource(R.xml.preferences);
        sharedPref = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        final android.support.v7.preference.EditTextPreference userNamePreference = (android.support.v7.preference.EditTextPreference) findPreference("key_changeUsername");
        final android.support.v7.preference.EditTextPreference userEmailPreference = (android.support.v7.preference.EditTextPreference) findPreference("key_changeEmail");
        final android.support.v7.preference.EditTextPreference userPasswordPreference = (android.support.v7.preference.EditTextPreference) findPreference("key_changePassword");

          currentUserName = getString(R.string.empty_string, sharedPref.getString(getString(R.string.prefs_loggedin_username),"Default value"));

        userNamePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if(!newValue.toString().isEmpty() && !LoginRegisterActivity.usernameFound(newValue.toString()))
                {
                    SimpleDialog.create(DialogOption.OkOnlyDismiss, getContext(), "Valid Username", "The username was changed successfully!").show();
                    changeUserDetails(db, 1, currentUserName, newValue.toString());
                    editor.remove("loggedInUsername");
                    editor.putString("loggedInUsername", newValue.toString());
                    editor.apply();

                } else {
                    SimpleDialog.create(DialogOption.OkOnlyDismiss, getContext(), "Invalid Username", "Please use another username").show();
                }
                return true;
            }
        });

        userEmailPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if(!newValue.toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(newValue.toString()).matches())
                {
                    SimpleDialog.create(DialogOption.OkOnlyDismiss, getContext(), "Valid email", "The email was changed successfully! : " + newValue.toString()).show();
                    changeUserDetails(db,3, currentUserName, newValue.toString()); //change in db and array
                } else {
                    SimpleDialog.create(DialogOption.OkOnlyDismiss, getContext(), "Invalid email", "Please use valid email format!").show();
                }
                return true;
            }
        });

        userPasswordPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if(TextUtils.isEmpty(newValue.toString()))
                {
                    SimpleDialog.create(DialogOption.OkOnlyDismiss, getContext(), "Invalid password", "Please enter your new password again!").show();
                } else {
                    SimpleDialog.create(DialogOption.OkOnlyDismiss, getContext(), "Valid password", "The password was changed successfully!").show();
                    changeUserDetails(db, 2, currentUserName, newValue.toString()); // edit array list
                }
                return true;
            }
        });



        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("key_checkbox1");
        final CheckBoxPreference checkboxPref2 = (CheckBoxPreference) getPreferenceManager().findPreference("key_checkbox_newEpisode");
        final CheckBoxPreference checkboxPref3 = (CheckBoxPreference) getPreferenceManager().findPreference("key_checkbox_trailerN");

        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if(checkboxPref.isChecked())
                {
                    editor.remove(getString(R.string.prefs_loggedin_username));
                    editor.putBoolean(getString(R.string.prefs_loggedin_boolean), false).apply();
                    editor.apply();
                    stayLoggedIn = false;
                } else {
                    editor.putString("loggedInUsername", currentUserName);
                    editor.putBoolean(getString(R.string.prefs_loggedin_boolean), true).apply();
                    editor.apply();
                    stayLoggedIn = true;
                }

                return true;
            }
        });





    }

    public  void changeUserDetails(FirebaseFirestore db,  int changeIt, String username, final String newValue)
    {
        int size = LoginRegisterActivity.userList.size();
        for(int i = 0; i < size; i++)
        {
            if (LoginRegisterActivity.userList.get(i).getUsername().equals(username)) // who to change details for?
            {
               if(changeIt == 1) {
                   final String username1 = username;
                   final FirebaseFirestore db1 = db;
                   db.collection("UserDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                       @Override
                       public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                           for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                               if (document.getId().equals(username1)) {
//                                   SimpleDialog.create(DialogOption.OkOnlyDismiss, getContext(), "...",
//                                           "dob: " + document.getData().get("dob").toString()
//                                                   + "\n email: " + document.getData().get("email").toString()
//                                                   + "\n password: " + document.getData().get("password").toString()
//                                                   + "\n join_date: " + document.getData().get("join_date").toString()
//                                                   + "\n timeWatched: " + document.getData().get("timeWatched").toString()
//                                                   + "\n genresWatched: " + document.getData().get("genresWatched")
//                                            ).show();


                                   Map<String, Object> user = new HashMap<>();
                                   user.put("dob", document.getData().get("dob").toString());
                                   user.put("email", document.getData().get("email").toString());
                                   user.put("password", document.getData().get("password").toString());
                                   user.put("join_date", document.getData().get("join_date"));
                                   user.put("timeWatched", document.getData().get("timeWatched"));
                                   user.put("genresWatched", document.getData().get("genresWatched"));

                                   db1.collection("UserDetails").document(username1).delete();

                                   db1.collection("UserDetails").document(newValue.toString()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           Log.d("Success", "DocumentSnapshot successfully written!");
                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Log.w("Error", "Error writing document", e);
                                       }
                                   });
                               }
                           }
                       }
                   });
                   LoginRegisterActivity.userList.get(i).setUsername(newValue);
               } else if(changeIt == 2) {


                   String newHashedPass = RegisterFragment.hash(newValue.toString());
                   LoginRegisterActivity.userList.get(i).setPassword(newHashedPass);
                   DocumentReference docRef2 = db.collection("UserDetails").document(username);
                   docRef2.update("password", newHashedPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Log.d("Success", "password successfully changed!" + " New password is : " + newValue.toString());
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.w("Error", "Error editing password.");
                       }
                   });
                   ;
               }
                    else if(changeIt == 3) {

                        final int k = i;
                        DocumentReference docRef3 = db.collection("UserDetails").document(username);
                        docRef3.update("email", newValue.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Success", "Email successfully changed!" + " New email is : " + newValue.toString());
                                LoginRegisterActivity.userList.get(k).setEmail(newValue.toString()); // change in array list
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Error", "Error editing email.");
                            }
                        });;

                }


            } else {
           //     Log.d("No username was found.", " New value: " + newValue.toString());
            }
        }
    }

}
