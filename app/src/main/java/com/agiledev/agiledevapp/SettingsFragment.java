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

import java.util.ArrayList;


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

//    @Override
//   public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preferences);
//
//
//    }
//      final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("key_checkbox1");
//
//
//        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
//                SharedPreferences.OnSharedPreferenceChangeListener() {
//                    @Override
//                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
//                                                          String key) {
//                        // your stuff here
//                    }
//                };


//        };

     //   CheckBoxPreference c = new CheckBoxPreference();
    //    Button b = new Button();
   //     CheckBoxPreference box =   ((SettingsFragment)getParentFragment()).findPreference("key_checkbox1");

//        for(int i = 0; i < pref.size(); i++)
//       {
//            pref.get(i).setSummary("key_passwordkey_password");
//        }
//
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


        android.support.v7.preference.EditTextPreference userNamePreference = (android.support.v7.preference.EditTextPreference) findPreference("key_changeUsername");
        android.support.v7.preference.EditTextPreference userEmailPreference = (android.support.v7.preference.EditTextPreference) findPreference("key_changeEmail");

        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("key_checkbox1");

        //default text of change username pop up settings is the current username
        userNamePreference.setText(getString(R.string.empty_string, sharedPref.getString(getString(R.string.prefs_loggedin_username),"Default value")));
      //  userEmailPreference.setText(getString(R.string.empty_string, sharedPref.getString(getString(R.string.prefs_loggedin_email),"Default value")));
      //  userEmailPreference.setText(getString(R.string.empty_string, sharedPref.getString(getString(R.string.prefs_loggedin_email),"Default value")));


     //   userNamePreference.setText(sharedPref.getString(String.valueOf(R.string.prefs_loggedin_username), "defautl value")); // default value is the current username
     //  userNamePreference.setDefaultValue(sharedPref.getString(String.valueOf(R.string.prefs_loggedin_username).toString(), "d"));



    //    textView.setText(getString(R.string.nav_loggedin_as, sharedPref.getString(getString(R.string.prefs_loggedin_username),"Error, user not found!")));

      //  userNamePreference.setDefaultValue(sharedPref.getString(R.string.prefs_loggedin_username));


        //sharedPref.getString(getString(R.string.prefs_loggedin_username)
      //  sharedPref.getString(getString(R.string.prefs_loggedin_username))

     //   checkboxPref.setSummary(String.valueOf(changUss.getText().toString()));

     //   System.out.println(String.valueOf(changUss.getText().toString()));

       // checkboxPref.setSummary("suumma");
        // changUss.setSummary("suumma");

            //password goes into fb
        //to do username create onclick listener here, when accept is clicked new create new shared preference and replace/add new username from textfield string
       // loginergisteractivity.java = see
    }
}