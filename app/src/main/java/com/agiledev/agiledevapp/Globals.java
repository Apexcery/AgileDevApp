package com.agiledev.agiledevapp;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Globals
{

    //Lists
    private static SparseArray<String> genreTags = new SparseArray<>();
    private static List<trackedMovie> trackedMovies = new ArrayList<>();
    private static List<trendingMovie> trendingMovies = new ArrayList<>();
    private static List<trendingTvShow> trendingTvShows = new ArrayList<>();
//    private static Map<String, Object> trackedMovies = new HashMap<>();

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
        String id;
        Date date;
        String poster_path;

        @Override
        public int compareTo(@NonNull trackedMovie o) {
            return o.date.compareTo(date);
        }
    }

    //-------Trending Movie--------
    public static class trendingMovie {
        String id;
        String poster_path;
        Float vote_average;
    }

    public static List<trendingMovie> getTrendingMovies()
    {
            return trendingMovies;
    }

    public static void setTrendingMovies(List<trendingMovie> trendingMovies)
    {
            Globals.trendingMovies = trendingMovies;
    }

    public static void addToTrendingMovies(trendingMovie movie)
    {
            Globals.trendingMovies.add(movie);
    }


    //-------Trending TvShow--------
    public static class trendingTvShow
    {
        String id;
        String poster_path;
        Float vote_average;
    }

    public static List<trendingTvShow> getTrendingTvShows()
    {
        return trendingTvShows;
    }

    public static void setTrendingTvShows(List<trendingTvShow> trendingTvShows)
    {
        Globals.trendingTvShows = trendingTvShows;
    }

    public static void addToTrendingTvShows(trendingTvShow tvshow)
    {
        Globals.trendingTvShows.add(tvshow);
    }


}
