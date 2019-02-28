package com.agiledev.agiledevapp;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

        return view;
    }

    public void populateRecentMovies() {
        List<Globals.Movie> recentMovies = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.moviesHomeRecentlyWatchedRecycler);

        recentMovies = getRecentMovies();

//        RecentMoviesAdapter adapter = new RecentMoviesAdapter(getActivity(), recentMovies, ((FragmentActivity)getActivity()).getSupportFragmentManager());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public List<Globals.Movie> getRecentMovies() {
        final ArrayList<Globals.Movie> movieList = new ArrayList<>();
        db.collection("TrackedMovies").document(sharedPref.getString(getString(R.string.prefs_loggedin_username), null)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Map<String, Object> movies = doc.getData();
                        for (Map.Entry<String, Object> entry : movies.entrySet()) {
                            Globals.Movie movie = new Globals.Movie();
                            movie.id = entry.getKey();
                            Map<String, Object> field = (Map)entry.getValue();
                            Timestamp timestamp = (Timestamp)field.get("date");
                            movie.date = timestamp.toDate();
                            movieList.add(movie);
                        }
                        if (movieList.size() > 0) {
                            Collections.sort(movieList, new Comparator<Globals.Movie>() {
                                @Override
                                public int compare(Globals.Movie o1, Globals.Movie o2) {
                                    return o2.date.compareTo(o1.date);
                                }
                            });
                        }
                    }
                }
            }
        });
        List<Globals.Movie> returnList = movieList.subList(0, 10);
        return returnList;
    }

//    public ArrayList<BasicMovieDetails> getMovieImages(List<Globals.Movie> movieList) {
//        for (Globals.Movie m : movieList) {
//            TmdbClient.getMovieImages(m.id, null, new JsonHttpResponseHandler() {
//
//            });
//        }
//    }
}
