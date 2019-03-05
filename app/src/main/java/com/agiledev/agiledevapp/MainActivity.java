package com.agiledev.agiledevapp;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.DocumentCollections;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager = getFragmentManager();

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            return;
        }

        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        TmdbClient.key = getResources().getString(R.string.tmdb_api_key);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final TextView logout = findViewById(R.id.nav_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.loggedInUser);
        textView.setText(getString(R.string.nav_loggedin_as, sharedPref.getString(getString(R.string.prefs_loggedin_username),"Error, user not found!")));

        fragmentManager.beginTransaction().replace(R.id.content_frame,new HomeFragment()).commit();

        populateTrackedMovies();
        populateGenreTags();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //finish();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            ,new HomeFragment())
                    .commit();

        } else if (id == R.id.nav_movies) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            ,new MovieFragment())
                    .commit();

        } else if (id == R.id.nav_tv) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            ,new TrackedTvshowsFragment())
                    .commit();

        } else if (id == R.id.nav_help) {
            removeAllFragments(fragmentManager);
            Intent intent = new Intent(this, ActivityHelp.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    editor.remove(getString(R.string.prefs_loggedin_username));
                    editor.remove(getString(R.string.prefs_loggedin_boolean));
                    editor.apply();

                    finishAffinity();
                    Intent intent = new Intent(getBaseContext(), LoginRegisterActivity.class);
                    getBaseContext().startActivity(intent);
                } else if (i == DialogInterface.BUTTON_NEGATIVE)
                    dialogInterface.dismiss();
            }
        };
        AlertDialog dialog = SimpleDialog.create(DialogOption.YesCancel, this,"Logout?", "Are you sure you want to logout?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", dialogClick);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", dialogClick);
        dialog.show();
    }

    //TODO: Move this method to the splash screen activity.
    public synchronized void populateGenreTags() {
        TmdbClient.getGenres(null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray results = new JSONArray();
                try {
                    results = response.getJSONArray("genres");
                } catch (JSONException e) {
                    Log.e("JSON Error", e.getMessage());
                    e.printStackTrace();
                }
                SparseArray<String> genres = new SparseArray<>();
                for (int i = 0; i < results.length(); i++) {
                    try {
                        JSONObject genre = results.getJSONObject(i);
                        genres.put(genre.getInt("id"), genre.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Globals.setGenreTags(genres);
            }
        });
    }

    public synchronized void populateTrackedMovies() {
        DocumentReference trackedMoviesRef = FirebaseFirestore.getInstance().collection("TrackedMovies").document(sharedPref.getString(getString(R.string.prefs_loggedin_username), null));
        trackedMoviesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> movies = document.getData();
                        List<Globals.Movie> movieList = new ArrayList<>();
                        for(Map.Entry<String, Object> entry : movies.entrySet()) {
                            Globals.Movie movie = new Globals.Movie();
                            movie.id = entry.getKey();
                            Map<String, Object> field = (Map)entry.getValue();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            Timestamp timestamp = (Timestamp)field.get("date");
                            movie.date = timestamp.toDate();
                            movieList.add(movie);
                        }
                        Globals.setTrackedMovies(movieList);
                    }
                }
            }
        });
    }
}
