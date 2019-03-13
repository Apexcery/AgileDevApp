package com.agiledev.agiledevapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.view.Gravity.CENTER_VERTICAL;

public class SearchResultsActivity extends AppCompatActivity {

    ProgressBar spinner;
    RecyclerView recyclerView;
    MoviesAdapter adapter;
    List<BasicMovieDetails> movies = new ArrayList<>();
    String searchPhrase;
    View v;
    LinearLayout searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        v = this.getWindow().getDecorView().findViewById(android.R.id.content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.search_recycler_view);
        spinner = findViewById(R.id.searchLoadingSpinner);
        searchResults = findViewById(R.id.searchResults);

        handleIntent(getIntent());

        searchMovieByTitle(searchPhrase);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        Spinner spinner = (Spinner)menu.findItem(R.id.type).getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.media_type, R.layout.app_bar_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        LinearLayout layout = (LinearLayout)searchView.getChildAt(0);
        layout.addView(spinner);
        layout.setGravity(CENTER_VERTICAL);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        return true;
    }

    protected void onNewIntent(Intent intent) {
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchPhrase = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
            suggestions.saveRecentQuery(searchPhrase, null);
        }
    }

    protected synchronized void searchMovieByTitle(String title) {
        TmdbClient.searchMoviesByQuery(title,null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray results = new JSONArray();
                try {
                    results = response.getJSONArray("results");
                } catch (JSONException e) {
                    Log.e("JSON Error", e.getMessage());
                    if(e.getMessage().equals("No value for Search")) {
                        final Snackbar noResults = Snackbar.make(findViewById(R.id.searchResultsLayout), "No results found.", Snackbar.LENGTH_INDEFINITE);
                        noResults.setActionTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary)).setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                noResults.dismiss();
                            }
                        });
                        noResults.show();
                    }
                }
                for (int i = 0; i < results.length(); i++) {
                    try {
                        Log.e("Results:", results.get(i).toString());
                        BasicMovieDetails movie = new Gson().fromJson(results.get(i).toString(), BasicMovieDetails.class);
                        movies.add(movie);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new MoviesAdapter(getBaseContext(), movies, getSupportFragmentManager());
                spinner.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
                searchResults.setVisibility(View.VISIBLE);
            }
        });
    }
}
