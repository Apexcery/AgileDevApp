package com.agiledev.agiledevapp;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchResultsActivity extends AppCompatActivity {

    ProgressBar spinner;
    RecyclerView recyclerView;
    MoviesAdapter adapter;
    List<Movie> movies = new ArrayList<>();
    String searchPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.search_recycler_view);
        spinner = findViewById(R.id.searchLoadingSpinner);

        handleIntent(getIntent());

        searchMovieByTitle(searchPhrase);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(15), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount, spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column  * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount)  {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    protected void onNewIntent(Intent intent) {
        handleIntent(getIntent());
    }

    private synchronized void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchPhrase = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    protected synchronized void searchMovieByTitle(String title) {
        OmdbRestClient.get("s=" + title, null, new JsonHttpResponseHandler() {
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
                adapter = new MoviesAdapter(getBaseContext(), movies);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
