package com.agiledev.agiledevapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MediaTracking {

    public static enum Media {
        MOVIE,
        TV
    }

    static String title, poster_path;
    private static ArrayList genreList;
    private static int runtime;

    public static AlertDialog trackMovie(final Context mContext, final View mView, final String username, final String id) {
        final AlertDialog dialog = SimpleDialog.create(DialogOption.YesCancel, mContext, "Track Movie", "Are you sure you want to track this movie?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final DocumentReference movieRef = FirebaseFirestore.getInstance().collection("TrackedMovies").document(username);
                movieRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final DocumentSnapshot doc = task.getResult();
                            TmdbClient.getMovieInfo(id, null, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    FullMovieDetails movieDetails = new Gson().fromJson(response.toString(), FullMovieDetails.class);
                                    if (movieDetails == null)
                                        return;
                                    title = movieDetails.getTitle();
                                    poster_path = movieDetails.getPoster_path();
                                    genreList = movieDetails.getGenres();
                                    runtime = movieDetails.getRuntime();

                                    Map<String, Object> trackedMovie = new HashMap<>();
                                    Map<String, Object> trackData = new HashMap<>();
                                    trackData.put("date", new Date());
                                    trackData.put("name", title);
                                    trackData.put("poster_path", poster_path);

                                    Map<String, String> genres = new HashMap<>();
                                    for (FullMovieDetails.Genre g : (ArrayList<FullMovieDetails.Genre>)genreList) {
                                        genres.put(String.valueOf(g.id), g.name);
                                    }
                                    trackData.put("genres", genres);

                                    trackedMovie.put(id, trackData);
                                    if (!doc.exists()) {
                                        movieRef.set(trackedMovie);
                                    } else {
                                        movieRef.update(trackedMovie);
                                    }
                                    Globals.trackedMovie movie = new Globals.trackedMovie();
                                    movie.id = id;
                                    movie.date = new Date();
                                    movie.poster_path = poster_path;
                                    movie.name = title;
                                    for (HashMap.Entry<String, String> e : genres.entrySet()) {
                                        movie.genres.put(Integer.parseInt(e.getKey()), e.getValue());
                                    }
                                    Globals.addToTrackedMovies(movie);
                                    Globals.sortTrackedMovies();

                                    final DocumentReference userRef = FirebaseFirestore.getInstance().collection("UserDetails").document(username);
                                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot doc = task.getResult();
                                                Map<String, Object> userData = new HashMap<>();
                                                userData.put("timeWatched", Integer.valueOf(doc.get("timeWatched").toString()) + runtime);
                                                Map<String, Long> userGenres = (Map<String, Long>)doc.get("genresWatched");
                                                for (FullMovieDetails.Genre g : (ArrayList<FullMovieDetails.Genre>)genreList) {
                                                    if (userGenres.containsKey(g.name)) {
                                                        userGenres.put(g.name, userGenres.get(g.name) + 1);
                                                    } else {
                                                        userGenres.put(g.name, 1L);
                                                    }
                                                }
                                                userData.put("genresWatched", userGenres);
                                                userRef.update(userData);

                                                Snackbar.make(mView, "Movie Tracked!", Snackbar.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            });
                        }
                    }
                });
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    public static void trackTV() {

    }

    public static AlertDialog untrackMovie(final Context mContext, final View mView, final String username, final String id) {
        final AlertDialog dialog = SimpleDialog.create(DialogOption.YesCancel, mContext, "Untrack Movie", "Are you sure you want to untrack this movie?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DocumentReference ref = FirebaseFirestore.getInstance().collection("TrackedMovies").document(username);
                ref.update(id, FieldValue.delete());

                Globals.removeFromTrackedMovies(id);

                TmdbClient.getMovieInfo(id, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        FullMovieDetails movieDetails = new Gson().fromJson(response.toString(), FullMovieDetails.class);
                        if (movieDetails == null)
                            return;
                        genreList = movieDetails.getGenres();

                        final DocumentReference userRef = FirebaseFirestore.getInstance().collection("UserDetails").document(username);
                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("timeWatched", Integer.valueOf(doc.get("timeWatched").toString()) - runtime);
                                    Map<String, Long> userGenres = (Map<String, Long>)doc.get("genresWatched");
                                    for (FullMovieDetails.Genre g : (ArrayList<FullMovieDetails.Genre>)genreList) {
                                        userGenres.put(g.name, userGenres.get(g.name) - 1);
                                    }
                                    userData.put("genresWatched", userGenres);
                                    userRef.update(userData);

                                    Snackbar.make(mView, "Movie Untracked!", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    public static void untrackTV() {

    }
}
