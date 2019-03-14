package com.agiledev.agiledevapp;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public static synchronized void setGenreTags(SparseArray<String> genres) {
        genreTags = genres;
    }

    //Tracked movies configurators
    public static List<trackedMovie> getTrackedMovies() {
        return trackedMovies;
    }
    public static synchronized void setTrackedMovies(List<trackedMovie> trackedMovies) {
        Globals.trackedMovies = trackedMovies;
    }
    public static synchronized void addToTrackedMovies(trackedMovie movie) {
        Globals.trackedMovies.add(movie);
    }
    public static synchronized void removeFromTrackedMovies(String id) {
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
    public static synchronized void sortTrackedMovies() {
        if (Globals.trackedMovies.size() > 0) {
            Collections.sort(Globals.trackedMovies, new Comparator<trackedMovie>() {
                @Override
                public int compare(Globals.trackedMovie o1, Globals.trackedMovie o2) {
                    return o2.date.compareTo(o1.date);
                }
            });
        }
    }
    public static class trackedMovie implements Comparable<trackedMovie> {
        String id, name;
        Date date;
        String poster_path;
        SparseArray<String> genres = new SparseArray<>();

        @Override
        public int compareTo(@NonNull trackedMovie o) {
            return o.date.compareTo(date);
        }
    }
}
