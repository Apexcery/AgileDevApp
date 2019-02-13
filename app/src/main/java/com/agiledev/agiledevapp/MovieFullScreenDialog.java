package com.agiledev.agiledevapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

import cz.msebera.android.httpclient.Header;

public class MovieFullScreenDialog extends DialogFragment {

    public static String TAG = "FullScreenDialog";
    public String imdbID;
    public FullMovieDetails movieDetails;
    public TextView toolbarTitle;

    public static MovieFullScreenDialog newInstance(String id) {
        MovieFullScreenDialog fragment = new MovieFullScreenDialog();
        Bundle args = new Bundle();
        args.putString("imdbID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.movie_dialog_layout, container, false);

        Toolbar toolbar = view.findViewById(R.id.dialogToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        toolbarTitle = view.findViewById(R.id.movieTitle);

        String imdbID = getArguments().getString("imdbID", "No Title Found");
        this.imdbID = imdbID;
        getMovieDetails();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    protected synchronized void getMovieDetails() {
        OmdbRestClient.get("i=" + imdbID + "&plot=full", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                movieDetails = new Gson().fromJson(response.toString(), FullMovieDetails.class);
                if (movieDetails == null)
                    return;
                toolbarTitle.setText(movieDetails.getTitle());
            }
        });
    }
}
