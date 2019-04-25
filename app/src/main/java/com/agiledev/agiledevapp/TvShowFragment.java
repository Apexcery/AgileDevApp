package com.agiledev.agiledevapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

        TextView title = view.findViewById(R.id.tvShowsHomeRecommendedTitle);
        String recTitle = "Recommended because you watched: <font color='#ec2734'>" + randomTv.name + "</font>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            title.setText(Html.fromHtml(recTitle, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            title.setText(Html.fromHtml(recTitle), TextView.BufferType.SPANNABLE);
        }

        TmdbClient.getRelatedTV(randomTv.id, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray results = new JSONArray();
                try {
                    results = response.getJSONArray("results");
                } catch (JSONException e) {
                    Log.e("JSON Error", e.getMessage());
                }

                List<Globals.trackedTV> bmd = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    try {
                        BasicTvShowDetails tv = new Gson().fromJson(results.getJSONObject(i).toString(), BasicTvShowDetails.class);
                        Globals.trackedTV t = new Globals.trackedTV();
                        if (tv.getId().equals(randomTv.id))
                            continue;
                        t.id = tv.getId();
                        t.poster_path = tv.getPoster_path();
                        t.name = tv.getName();
                        bmd.add(t);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                bmd = new ArrayList<>(bmd.subList(0, min(bmd.size(), 9)));
                RecyclerView recyclerView = view.findViewById(R.id.tvShowsHomeRecommendedRecycler);

                RecentTvShowsAdapter adapter = new RecentTvShowsAdapter(getActivity(), bmd, getActivity().getSupportFragmentManager());
                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void populateRecommendedInArea() {
        //TODO:Use GPS to pull user's area and show recommended TvShows based upon it.
    }
}