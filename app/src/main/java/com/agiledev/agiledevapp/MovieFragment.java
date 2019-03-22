package com.agiledev.agiledevapp;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

import static java.lang.Math.min;

public class MovieFragment extends Fragment {

    View view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String genreString;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movies, container, false);
        getActivity().setTitle(R.string.movies_name);
        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        populateRecentMovies();
        populateRecommendedForUser();
        populateRecommendedInArea();

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.moviesRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                ,new MovieFragment())
                        .commit();
            }
        });
        return view;
    }

    private void populateRecommendedInArea() {
        //TODO:Use GPS to pull user's area and show recommended movies based upon it.
    }

    private void populateRecommendedForUser() {
    //TODO:Use the genres of what the user has tracked and show recommended movies based upon it.
        if (Globals.getTrackedMovies().size() <= 0)
            return;
        List<Globals.trackedMovie> trackedMovies = Globals.getTrackedMovies();
        trackedMovies = new ArrayList<>(trackedMovies.subList(0, min(trackedMovies.size(), 10)));
        Globals.trackedMovie randomMovie = trackedMovies.get(new Random().nextInt(trackedMovies.size()));

        genreString = "";
        for(int i = 0; i < randomMovie.genres.size(); i++)
        {
            genreString += randomMovie.genres.keyAt(i);
            if (i < randomMovie.genres.size()){
                genreString += ",";
            }
        }
        TextView title = view.findViewById(R.id.moviesHomeRecommendedTitle);
        title.setText("Recommended because you watched: " + randomMovie.name);
        TmdbClient.getRelatedMovies(genreString, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                JSONArray results = new JSONArray();
                try {
                    results = response.getJSONArray("results");
                } catch (JSONException e) {
                    Log.e("JSON Error", e.getMessage());
                    e.printStackTrace();
                }

                List<Globals.trackedMovie> bmd = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    try{
                        BasicMovieDetails movie = new Gson().fromJson(results.getJSONObject(i).toString(), BasicMovieDetails.class);
                        Globals.trackedMovie m = new Globals.trackedMovie();
                        m.id = movie.getId();
                        m.poster_path = movie.getPoster_path();
                        m.name = movie.getTitle();
                        bmd.add(m);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                bmd = new ArrayList<>(bmd.subList(0, min(bmd.size(), 10)));
                RecyclerView recyclerView = view.findViewById(R.id.moviesHomeRecommendedRecycler);

                RecentMoviesAdapter adapter = new RecentMoviesAdapter(getActivity(), bmd, getActivity().getSupportFragmentManager());

                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

            }
        } );






    }

    public void populateRecentMovies() {
        List<Globals.trackedMovie> recentMovies = Globals.getTrackedMovies();

        List<Globals.trackedMovie> tenRecentMovies = new ArrayList<>(recentMovies.subList(0, min(recentMovies.size(), 10)));

        RecyclerView recyclerView = view.findViewById(R.id.moviesHomeRecentlyWatchedRecycler);
//        RecentMoviesAdapter adapter = new RecentMoviesAdapter(getActivity(), , ((FragmentActivity)getActivity()).getSupportFragmentManager());

        RecentMoviesAdapter adapter = new RecentMoviesAdapter(getActivity(), tenRecentMovies, getActivity().getSupportFragmentManager());

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
