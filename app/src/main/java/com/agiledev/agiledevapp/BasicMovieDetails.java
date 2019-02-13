package com.agiledev.agiledevapp;

public class BasicMovieDetails {

    private String Title;
    private String Year;
    private String Poster;
    private String imdbID;

    public BasicMovieDetails() {
    }

    public BasicMovieDetails(String title, String year, String posterUrl, String imdbID) {
        this.Title = title;
        this.Year = year;
        this.Poster = posterUrl;
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
}
