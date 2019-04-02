package com.agiledev.agiledevapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class TvShowFragment extends Fragment {

    View view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String genreString;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tvshow, container, false);
        getActivity().setTitle(R.string.tvshows_name);
        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        populateRecentTvShows();
        populateRecommendedForUser();
        //populateRecommendedInArea();

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.tvShowsRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                ,new TvShowFragment())
                        .commit();
            }
        });
        return view;
    }

    private void populateRecommendedInArea() {
        //TODO:Use GPS to pull user's area and show recommended TvShows based upon it.
    }

    private void populateRecommendedForUser() {
        //TODO:Use the genres of what the user has tracked and show recommended movies based upon it.
        if (Globals.getTrackedTvShows().size() <= 0)
            return;
        List<Globals.trackedTV> trackedTvShows = Globals.getTrackedTvShows();
        trackedTvShows = new ArrayList<>(trackedTvShows.subList(0, min(trackedTvShows.size(), 10)));
        Globals.trackedTV randomTV = trackedTvShows.get(new Random().nextInt(trackedTvShows.size()));

        genreString = "";
        for(int i = 0; i < randomTV.genres.size(); i++)
        {
            genreString += randomTV.genres.keyAt(i);
            if (i < randomTV.genres.size()){
                genreString += ",";
            }
        }
        TextView title = view.findViewById(R.id.tvshowsHomeRecommendedTitle);
        title.setText("Recommended because you watched: " + randomTV.title);
        TmdbClient.getRelatedTvshows(genreString, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                JSONArray results = new JSONArray();
                try {
                    results = response.getJSONArray("results");
                } catch (JSONException e) {
                    Log.e("JSON Error", e.getMessage());
                    e.printStackTrace();
                }

                List<Globals.trackedTV> bmd = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    try{
                        BasicTvShowDetails tv = new Gson().fromJson(results.getJSONObject(i).toString(), BasicTvShowDetails.class);
                        Globals.trackedTV m = new Globals.trackedTV();
                        m.id = tv.getId();
                        m.poster_path = tv.getPoster_path();
                        m.title = tv.getName();
                        bmd.add(m);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                bmd = new ArrayList<>(bmd.subList(0, min(bmd.size(), 10)));
                RecyclerView recyclerView = view.findViewById(R.id.tvshowsHomeRecommendedRecycler);

                RecentTvShowsAdapter adapter = new RecentTvShowsAdapter(getActivity(), bmd, getActivity().getSupportFragmentManager());

                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

            }
        } );






    }

    public void populateRecentTvShows() {
        List<Globals.trackedTV> recentTvShows = Globals.getTrackedTvShows();

        List<Globals.trackedTV> tenRecentTvShows = new ArrayList<>(recentTvShows.subList(0, min(recentTvShows.size(), 10)));

        RecyclerView recyclerView = view.findViewById(R.id.tvShowsHomeRecentlyWatchedRecycler);

        RecentTvShowsAdapter adapter = new RecentTvShowsAdapter(getActivity(), tenRecentTvShows, getActivity().getSupportFragmentManager());

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}