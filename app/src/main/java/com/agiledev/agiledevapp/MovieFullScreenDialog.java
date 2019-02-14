package com.agiledev.agiledevapp;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieFullScreenDialog extends DialogFragment {

    public static String TAG = "FullScreenDialog";
    public String id;
    public FullMovieDetails movieDetails;
    public TextView toolbarTitle;
    public ImageView trailerVideoImage, trailerVideoPlayImage;

    public static MovieFullScreenDialog newInstance(String id) {
        MovieFullScreenDialog fragment = new MovieFullScreenDialog();
        Bundle args = new Bundle();
        args.putString("id", id);
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
        trailerVideoImage = view.findViewById(R.id.movieTrailerImage);
        trailerVideoPlayImage = view.findViewById(R.id.movieTrailerPlayIcon);

        this.id = getArguments().getString("id", "No Title Found");

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
        TmdbRestClient.get("movie/" + id + "?api_key=" + getResources().getString(R.string.tmdb_api_key) + "&append_to_response=videos", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                movieDetails = new Gson().fromJson(response.toString(), FullMovieDetails.class);
                if (movieDetails == null)
                    return;
                toolbarTitle.setText(movieDetails.getTitle());
                Uri uri = Uri.parse("https://image.tmdb.org/t/p/w1280" + movieDetails.getBackdrop_path());
                Glide.with(MovieFullScreenDialog.this).load(uri).listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        trailerVideoPlayImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).dontAnimate().into(trailerVideoImage);
            }
        });
    }

    protected String findTrailerKey(ArrayList<FullMovieDetails.Video> videos) {
        for (FullMovieDetails.Video v : videos) {
            if(v.type.equals("Trailer")) {
                return v.key;
            }
        }
        return null;
    }
}
