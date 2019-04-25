package com.agiledev.agiledevapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;

/**
 * Created by s6104158 on 07/02/19.
 */

public class TvShowFragment extends Fragment {

    View view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tvshow, container, false);
        getActivity().setTitle(R.string.tvshows_name);
        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        populateRecentTvShows();
        //populateRecommendedForUser();
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

    public void populateRecentTvShows() {
        List<Globals.trackedTV> recentTvShows = Globals.getTrackedTvShows();
        List<Globals.trackedTV> nineRecentTvShows = new ArrayList<>(recentTvShows.subList(0, min(recentTvShows.size(), 9)));

        RecyclerView recyclerView = view.findViewById(R.id.tvShowsHomeRecentlyWatchedRecycler);

        RecentTvShowsAdapter adapter = new RecentTvShowsAdapter(getActivity(), nineRecentTvShows, getActivity().getSupportFragmentManager());

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setVisibility(View.VISIBLE);
    }

    private void populateRecommendedForUser() {
        if (Globals.getTrackedTvShows().size() <= 0)
            return;
        List<Globals.trackedTV> trackedTVList = Globals.getTrackedTvShows();
        trackedTVList = new ArrayList<>(trackedTVList.subList(0, min(trackedTVList.size(), 9)));
        final Globals.trackedTV randomTv = trackedTVList.get(new Random().nextInt(trackedTVList.size()));

        String genreString = "";
    }

    private void populateRecommendedInArea() {
        //TODO:Use GPS to pull user's area and show recommended TvShows based upon it.
    }
}