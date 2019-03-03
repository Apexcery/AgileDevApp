package com.agiledev.agiledevapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class trendingTvShowsFragment extends Fragment
{
    ProgressBar spinner;
    RecyclerView recyclerView;
    MoviesAdapter adapter;
    List<FullTvShowDetails> tvshows = new ArrayList<>();
    View v;
    LinearLayout trendingtvResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_trendingtvshows, container, false);

        recyclerView = v.findViewById(R.id.tvtrending_recycler_view);
        spinner = v.findViewById(R.id.Tvtrendingspinner);
        trendingtvResults = v.findViewById(R.id.tvtrendingresults);

        getTrendingMovies();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return v;
    }

    private void getTrendingMovies()
    {
        TmdbClient.getweektrendingtvshows(null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray results = new JSONArray();
                try {
                    results = response.getJSONArray("results");

                    for (int i = 0; i < 10; i++) {
                        try {
                            Log.e("Results:", results.get(i).toString());
                            FullTvShowDetails tvshow = new Gson().fromJson(results.get(i).toString(), FullTvShowDetails.class);
                            tvshows.add(tvshow);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    TrendingTvShowsAdapter adapter = new TrendingTvShowsAdapter(getContext(), tvshows, getFragmentManager());
                    spinner.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                    trendingtvResults.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    Log.e("JSON Error", e.getMessage());

                }

            }
        });
    }
}
