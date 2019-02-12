package com.agiledev.agiledevapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by glees on 12/02/2019.
 */

public class Movie {

    private String Title;
    private String Year;
    private String Poster;

    public Movie() {
    }

    public Movie(String title, String year, String posterUrl) {
        this.Title = title;
        this.Year = year;
        this.Poster = posterUrl;
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
}
