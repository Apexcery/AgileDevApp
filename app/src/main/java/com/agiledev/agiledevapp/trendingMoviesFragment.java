package com.agiledev.agiledevapp;

import android.content.Context;
import android.net.Uri;
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


public class trendingMoviesFragment extends Fragment
{
    ProgressBar spinner;
    RecyclerView recyclerView;
    TrendingMoviesAdapter adapter;
    List<FullMovieDetails> movies = new ArrayList<>();
    View v;
    LinearLayout trendingMovieResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_trendingmovies, container, false);

        recyclerView = v.findViewById(R.id.movietrending_recycler_view);
        spinner = v.findViewById(R.id.movietrendingspinner);
        trendingMovieResults = v.findViewById(R.id.movietrendingresults);

        getTrendingMovies();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return v;
    }

    private void getTrendingMovies()
    {
        TmdbClient.getweektrendingmovies(null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray results = new JSONArray();
                try {
                    results = response.getJSONArray("results");

                    for (int i = 0; i < 10; i++) {
                        try {
                            Log.e("Results:", results.get(i).toString());
                            FullMovieDetails movie = new Gson().fromJson(results.get(i).toString(), FullMovieDetails.class);
                            movies.add(movie);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    adapter = new TrendingMoviesAdapter(getContext(), movies, getFragmentManager());
                    spinner.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                    trendingMovieResults.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    Log.e("JSON Error", e.getMessage());

                }

            }
        });
    }
}
