package com.agiledev.agiledevapp;

/**
 * Created by t7097354 on 12/02/19.
 */
import com.loopj.android.http.*;

public class OmdbRestClient {
    private static final String BASE_URL = "http://www.omdbapi.com/?apikey=80b6b1ac&";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
