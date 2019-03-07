package com.agiledev.agiledevapp;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Globals {
    private static SparseArray<String> genreTags = new SparseArray<>();
    private static List<Movie> trackedMovies = new ArrayList<>();
    private static List<trendingMovie> trendingMovies = new ArrayList<>();
//    private static Map<String, Object> trackedMovies = new HashMap<>();


    public static SparseArray<String> getGenreTags() {
        return genreTags;
    }
    public static void setGenreTags(SparseArray<String> genres) {
        genreTags = genres;
    }


    public static List<Movie> getTrackedMovies() {
        return trackedMovies;
    }
    public static void setTrackedMovies(List<Movie> trackedMovies) {
        Globals.trackedMovies = trackedMovies;
    }
    public static void addToTrackedMovies(Movie movie) {
        Globals.trackedMovies.add(movie);
    }
    public static void removeFromTrackedMovies(String id) {
        for (int i = 0; i < Globals.trackedMovies.size(); i++) {
            if (Globals.trackedMovies.get(i).id.equals(id)) {
                Globals.trackedMovies.remove(i);
                break;
            }
        }
    }
    public static boolean trackedMoviesContains(String id) {
        for (Movie m : Globals.trackedMovies) {
            if(m.id.equals(id))
                return true;
        }
        return false;
    }

    public static List<trendingMovie> getTrendingMovies() {
        return trendingMovies;
    }
    public static void setTrendingMovies(List<trendingMovie> trendingMovies) {
        Globals.trendingMovies = trendingMovies;
    }
    public static void addToTrendingMovies(trendingMovie movie) {
        Globals.trendingMovies.add(movie);
    }

    public static class Movie {
        String id;
        Date date;
    }
    public static class trendingMovie {
        String id;
        String poster_path;
        Float vote_average;
    }
}
