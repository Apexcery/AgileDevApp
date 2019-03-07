package com.agiledev.agiledevapp;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Globals {

    //Lists
    private static SparseArray<String> genreTags = new SparseArray<>();
    private static List<trackedMovie> trackedMovies = new ArrayList<>();

    //Genres getter and setter
    public static SparseArray<String> getGenreTags() {
        return genreTags;
    }
    public static void setGenreTags(SparseArray<String> genres) {
        genreTags = genres;
    }

    //Tracked movies configurators
    public static List<trackedMovie> getTrackedMovies() {
        return trackedMovies;
    }
    public static void setTrackedMovies(List<trackedMovie> trackedMovies) {
        Globals.trackedMovies = trackedMovies;
    }
    public static void addToTrackedMovies(trackedMovie movie) {
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
        for (trackedMovie m : Globals.trackedMovies) {
            if(m.id.equals(id))
                return true;
        }
        return false;
    }
    public static class trackedMovie {
        String id;
        Date date;
        String poster_path;
    }
}
