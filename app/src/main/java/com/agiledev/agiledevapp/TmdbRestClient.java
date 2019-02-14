package com.agiledev.agiledevapp;

import com.loopj.android.http.*;

public class TmdbRestClient {
//    private static final String BASE_URL = "http://www.omdbapi.com/?apikey=80b6b1ac&type=movie&";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
