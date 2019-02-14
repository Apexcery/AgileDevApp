package com.agiledev.agiledevapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by glees on 13/02/2019.
 */

public class FullMovieDetails {

    private boolean adult;
    private String backdrop_path;
    private ArrayList<Genre> genres;
    private String imdb_id;
    private String original_language;
    private String overview;
    private String poster_path;
    private ArrayList<ProductionCompanies> production_companies;
    private String release_date;
    private int runtime;
    private String status;
    private String tagline;
    private String title;
    private Videos videos;

    public FullMovieDetails() {}

    public FullMovieDetails(boolean adult, String backdrop_path, ArrayList<Genre> genres, String imdb_id, String original_language, String overview, String poster_path, ArrayList<ProductionCompanies> production_companies, String release_date, int runtime, String status, String tagline, String title, Videos videos) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.genres = genres;
        this.imdb_id = imdb_id;
        this.original_language = original_language;
        this.overview = overview;
        this.poster_path = poster_path;
        this.production_companies = production_companies;
        this.release_date = release_date;
        this.runtime = runtime;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.videos = videos;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public ArrayList<ProductionCompanies> getProduction_companies() {
        return production_companies;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Video> getVideos() {
        return videos.getResults();
    }

    public class Genre {
        int id;
        String name;
    }
    public class ProductionCompanies {
        int id;
        String name;
        String logo_path;
        String origin_country;
    }
    public class Videos {
        ArrayList<Video> results;

        public ArrayList<Video> getResults() {
            return results;
        }
    }
    public class Video {
        String id;
        String key;
        String name;
        String site;
        int size;
        String type;
    }
}
