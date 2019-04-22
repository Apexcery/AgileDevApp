package com.agiledev.agiledevapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
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

    public static AlertDialog trackTV(Context mContext, View mView, String type, String username, String seriesId, @Nullable Integer seasonNum, @Nullable Integer episodeNum) {
        switch (type) {
            case "series":

                break;
            case "season":

                break;
            case "episode":
                if (seasonNum != null && episodeNum != null)
                    return trackTVEpisode(mContext, mView, username, seriesId, seasonNum, episodeNum);
                else
                    Log.e("Tracking", "SeasonNum or EpisodeNum was null.");
                break;
            default:
                Log.e("Tracking", "Type was invalid.");
        }
        return null;
    }

    private static AlertDialog trackTVEpisode(final Context mContext, final View mView, final String username, final String seriesId, final int seasonNum, final int episodeNum) {
        final AlertDialog dialog = SimpleDialog.create(DialogOption.YesCancel, mContext, "Track TV", "Are you sure you want to track this episode?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final DocumentReference ref = FirebaseFirestore.getInstance().collection("TrackedTV").document(username);
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final DocumentSnapshot doc = task.getResult();
                            TmdbClient.getTvEpisodeDetails(seriesId, seasonNum, episodeNum, null, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    final FullTvEpisodeDetails episode = new Gson().fromJson(response.toString(), FullTvEpisodeDetails.class);
                                    if (episode == null)
                                        return;
                                    final Map<String, Object> trackedTV = new HashMap<>();
                                    final Map<String, Object> trackedSeason = new HashMap<>();
                                    final Map<String, Object> trackedEpisode = new HashMap<>();
                                    final Map<String, Object> trackData = new HashMap<>();

                                    trackData.put("date", new Date());
                                    trackData.put("episodeName", episode.getName());
                                    trackData.put("episodeNum", episode.getEpisode_number());

                                    TmdbClient.getFullTvShowDetails(seriesId, null, new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            final FullTvShowDetails show = new Gson().fromJson(response.toString(), FullTvShowDetails.class);
                                            if (show == null)
                                                return;

                                            genreList = show.getGenres();
                                            Map<String, String> genres = new HashMap<>();
                                            for (FullTvShowDetails.Genre g : (ArrayList<FullTvShowDetails.Genre>)genreList) {
                                                genres.put(String.valueOf(g.id), g.name);
                                            }
                                            trackData.put("genres", genres);
                                            trackData.put("id", episode.getId());
                                            trackData.put("seriesName", show.getName());

                                            String episodeString = "Episode " + episode.getEpisode_number();
                                            trackedEpisode.put(episodeString, trackData);
                                            String seasonString = "Season " + episode.getSeason_number();
                                            trackedSeason.put(seasonString, trackedEpisode);
                                            trackedTV.put(show.getId(), trackedSeason);

                                            if (!doc.exists())
                                                ref.set(trackedTV);
                                            else {
                                                if (doc.contains(show.getId())) { // Show exists
                                                    if (((HashMap)doc.get(show.getId())).containsKey(seasonString)) { // Season exists
                                                        if (!(((HashMap)((HashMap)doc.get(show.getId())).get(seasonString)).containsKey(episodeString))) { // Episode doesn't exist
                                                            HashMap currentEps = ((HashMap)((HashMap)doc.get(show.getId())).get(seasonString));
                                                            currentEps.put(episodeString, trackedEpisode.get(episodeString));
                                                            ref.update(show.getId() + "." + seasonString, currentEps);
                                                        }
                                                    } else {
                                                        HashMap currentSeasons = ((HashMap)doc.get(show.getId()));
                                                        currentSeasons.put(seasonString, trackedSeason.get(seasonString));
                                                        ref.update(show.getId(), currentSeasons);
                                                    }
                                                } else {
                                                    ref.update(trackedTV);
                                                }
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
