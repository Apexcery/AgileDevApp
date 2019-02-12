package com.agiledev.agiledevapp;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchResultsActivity extends AppCompatActivity {

    ProgressBar spinner;
    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.searchLoadingSpinner);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(getIntent());
    }

    private synchronized void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            OmdbRestClient.get("s=" + query, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray results = new JSONArray();
                    try {
                        results = response.getJSONArray("Search");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < results.length(); i++) {
                        try {
                            Log.e("Results:", results.get(i).toString());
                            Movie movie = new Gson().fromJson(results.get(i).toString(), Movie.class);
                            movies.add(movie);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinner.setVisibility(View.GONE);
                }
            });
        }
    }

    private class Movie {
        public String Title;
        public String Year;
        public String Poster;
    }
}
