package com.agiledev.agiledevapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by t7037453 on 26/02/19.
 */

public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        TmdbClient.key = getResources().getString(R.string.tmdb_api_key);

        populateGenreTags();
        getRecentMovies();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(getBaseContext(), LoginRegisterActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

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

    public void getRecentMovies() {
        final ArrayList<Globals.trackedMovie> movieList = new ArrayList<>();
        db.collection("TrackedMovies").document(sharedPref.getString(getString(R.string.prefs_loggedin_username), null)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Map<String, Object> movies = doc.getData();
                        for (Map.Entry<String, Object> entry : movies.entrySet()) {
                            Globals.trackedMovie movie = new Globals.trackedMovie();
                            movie.id = entry.getKey();
                            Map<String, Object> field = (Map)entry.getValue();
                            Timestamp timestamp = (Timestamp)field.get("date");
                            movie.date = timestamp.toDate();
                            movie.poster_path = (String)field.get("poster_path");
                            movieList.add(movie);
                        }
                        Globals.setTrackedMovies(movieList);
                        Collections.sort(Globals.getTrackedMovies());
                    }
                }
            }
        });
    }
}

