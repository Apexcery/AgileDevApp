package com.agiledev.agiledevapp;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.*;

class TmdbClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    static String key;

    private static AsyncHttpClient client = new AsyncHttpClient();

//  API Calls  //

    /**
     * This method is used to return a JSONArray of duration from the API.
     */
    static void getGenres(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String url = getAbsoluteUrl("genre/movie/list?api_key=" + key);
        client.get(url, params, responseHandler);
    }

    /**
     * This method is used to return information about a specific movie, using the movie's ID as the specifier.
     *
     * @param movieID The ID of the movie whose details will be pulled from the API.
     */
    static void getMovieInfo(String movieID, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String url = getAbsoluteUrl("movie/" + movieID + "?api_key=" + key + "&append_to_response=videos,credits");
        client.get(url, params, responseHandler);
    }

    /**
     * This method is used to search the API for movies that have the specified query in their name.
     *
     * @param query The query to search for, usually the movie title.
     */
    static void searchMoviesByQuery(String query, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String url = getAbsoluteUrl("search/movie?api_key=" + key + "&query=" + query);
        client.get(url, params, responseHandler);
    }

    /**
     * This method is used to pull information regarding a specific person, using their ID as the specifier.
     *
     * @param personID The ID of the person who's details should be queried.
     */
    static void getPersonDetails(int personID, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String url = getAbsoluteUrl("person/" + personID + "?api_key=" + key);
        client.get(url, params, responseHandler);
    }

    static void getMovieImages(String movieId, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String url = getAbsoluteUrl("movie/" + movieId + "/images?api_key=" + key);
        client.get(url, params, responseHandler);
    }

    static void getMovieCast(String movieID, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String url = getAbsoluteUrl("movie/" + movieID + "/credits?api_key=" + key);
        client.get(url, params, responseHandler);
    }

    /**
     * @param relativeUrl The specific part of the url after the BASE_URL that you want to request from.
     * @return The BASE_URL concatenated with the relative url that was passed as a parameter.
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

//  Image Loading  //

    /**
     * This method loads the specified image into the specified ImageView.
     *
     * @param path The relative path of the image to load.
     * @param holder The ImageView to load the image into.
     */
    static void loadImage(Context mContext, String path, ImageView holder, imageType type) {
        switch(type) {
            case SMALLICON:
                Glide.with(mContext).load(mContext.getResources().getString(R.string.poster_icon_base_url_small) + path).into(holder);
                break;
            case ICON:
                Glide.with(mContext).load(mContext.getResources().getString(R.string.poster_icon_base_url) + path).into(holder);
                break;
            case LARGEICON:
                Glide.with(mContext).load(mContext.getResources().getString(R.string.poster_icon_base_url_large) + path).into(holder);
                break;
        }
    }

    public enum imageType {
        SMALLICON,
        ICON,
        LARGEICON
    }
}
