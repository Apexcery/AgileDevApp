package com.agiledev.agiledevapp;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Globals
{

    //Lists
    private static SparseArray<String> movieGenreTags = new SparseArray<>();
    private static SparseArray<String> tvGenreTags = new SparseArray<>();
    private static List<trackedMovie> trackedMovies = new ArrayList<>();
    private static List<trackedTV> trackedTV = new ArrayList<>();
    private static List<trendingMovie> trendingMovies = new ArrayList<>();
    private static List<trendingTvShow> trendingTvShows = new ArrayList<>();
    private static SearchType lastSearchType = SearchType.Movie;

    //Genres getter and setter
    static SparseArray<String> getMovieGenreTags() {
        return movieGenreTags;
    }
    static synchronized void setMovieGenreTags(SparseArray<String> genres) {
        movieGenreTags = genres;
    }
    static SparseArray<String> getTvGenreTags() {
        return tvGenreTags;
    }
    static synchronized void setTvGenreTags(SparseArray<String> genres) {
        tvGenreTags = genres;
    }


    //Tracked media getters
    static List<trackedMovie> getTrackedMovies() {
        return trackedMovies;
    }

    static List<trackedTV> getTrackedTvShows() {
        return trackedTV;
    }


    //----------- Setters ------------
    static synchronized void setTrackedMovies(List<trackedMovie> trackedMovies) {
        Globals.trackedMovies = trackedMovies;
    }
    static synchronized void setTrackedTvShows(List<trackedTV> trackedTvShows) {
        Globals.trackedTV = trackedTvShows;
    }
    static synchronized void setLastSearchType(SearchType searchType) {
        Globals.lastSearchType = searchType;
    }
    static void setTrendingMovies(List<trendingMovie> trendingMovies)
    {
        Globals.trendingMovies = trendingMovies;
    }
    static void setTrendingTvShows(List<trendingTvShow> trendingTvShows)
    {
        Globals.trendingTvShows = trendingTvShows;
    }

    //----------- Getters ------------
    static SearchType getLastSearchType() {
        return lastSearchType;
    }
    static List<trendingMovie> getTrendingMovies()
    {
        return trendingMovies;
    }
    static List<trendingTvShow> getTrendingTvShows()
    {
        return trendingTvShows;
    }


    //----------- Adding ------------
    static synchronized void addToTrackedMovies(trackedMovie movie) {
        if (!trackedMoviesContains(movie.id))
            Globals.trackedMovies.add(movie);
    }
    static synchronized void addToTrackedTvShows(trackedTV TV) {
        if (!trackedTVContains(TV.id))
            Globals.trackedTV.add(TV);
    }
    static void addToTrendingMovies(trendingMovie movie) {
        Globals.trendingMovies.add(movie);
    }
    static void addToTrendingTvShows(trendingTvShow tvshow) {
        Globals.trendingTvShows.add(tvshow);
    }


    //----------- Removing ------------
    static synchronized void removeFromTrackedMovies(String id) {
        for (int i = 0; i < Globals.trackedMovies.size(); i++) {
            if (Globals.trackedMovies.get(i).id.equals(id)) {
                Globals.trackedMovies.remove(i);
                break;
            }
        }
    }
    static synchronized void removeFromTrackedTvShows(String id) {
        for (int i = 0; i < Globals.trackedTV.size(); i++) {
            if (Globals.trackedTV.get(i).id.equals(id)) {
                Globals.trackedTV.remove(i);
                break;
            }
        }
    }


    //----------- Contains ------------
    static boolean trackedMoviesContains(String id) {
        for (trackedMovie m : Globals.trackedMovies) {
            if(m.id.equals(id))
                return true;
        }
        return false;
    }
    static boolean trackedTVContains(String id) {
        for (trackedTV tv : Globals.trackedTV) {
            if(tv.id.equals(id))
                return true;
        }
        return false;
    }


    //----------- Sorting ------------
    static synchronized void sortTrackedMovies() {
        if (Globals.trackedMovies.size() > 0) {
            Collections.sort(Globals.trackedMovies, new Comparator<trackedMovie>() {
                @Override
                public int compare(Globals.trackedMovie o1, Globals.trackedMovie o2) {
                    return o2.date.compareTo(o1.date);
                }
            });
        }
    }
    static synchronized void sortTrackedTvShows() {
        if (Globals.trackedTV.size() > 0) {
            Collections.sort(Globals.trackedTV, new Comparator<trackedTV>() {
                @Override
                public int compare(Globals.trackedTV o1, Globals.trackedTV o2) {
                    return o2.date.compareTo(o1.date);
                }
            });
        }
    }


    //-------Tracked Movies--------
    static class trackedMovie implements Comparable<trackedMovie> {
        String id, name;
        Date date;
        String poster_path;
        SparseArray<String> genres = new SparseArray<>();

        @Override
        public int compareTo(@NonNull trackedMovie o) {
            return o.date.compareTo(date);
        }
    }

    //-------Tracked TV Shows--------
    static class trackedTV {
        String id, name;
        String poster_path;
        Date date;
        Map<String, ArrayList<Episode>> seasons = new HashMap<>();

        private Map<String, ArrayList<Episode>> getSeasons() {
            return seasons;
        }

        static class Episode implements Comparable<Episode> {
            Date date;
            String episodeName, id, seriesName;
            int episodeNum, seasonNum;
            SparseArray<String> genres = new SparseArray<>();

            @Override
            public int compareTo(@NonNull Episode episode) {
                return Integer.compare(episode.episodeNum, episodeNum);
            }
        }

        void addEpisode(Episode episode) {
            String seasonString = "Season " + episode.seasonNum;
            if (seasons.containsKey(seasonString)) { //Season exists
                if (!seasons.get(seasonString).contains(episode)) { //Episode doesn't exist
                    seasons.get(seasonString).add(episode);
                }
            } else {
                ArrayList<Episode> season = new ArrayList<>();
                season.add(episode);
                seasons.put(seasonString, season);
                Globals.addToTrackedTvShows(this);
            }
        }
    }


    //-------Trending Movie--------
    static class trendingMovie {
        String id;
        String poster_path;
        Float vote_average;
    }

    //-------Trending TvShow--------
    static class trendingTvShow
    {
        String id;
        String poster_path;
        Float vote_average;
    }

    //-------Last Search Type--------
    static enum SearchType {
        Movie,
        TV
    }
}
