package com.agiledev.agiledevapp;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class MovieFragment extends Fragment {

    View view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movies, container, false);

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
