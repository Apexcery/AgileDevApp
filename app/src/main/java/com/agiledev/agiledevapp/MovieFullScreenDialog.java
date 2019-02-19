package com.agiledev.agiledevapp;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MovieFullScreenDialog extends DialogFragment {

    public static String TAG = "FullScreenDialog";
    public String id;
    public FullMovieDetails movieDetails;
    public TextView toolbarTitle;
    public Toolbar toolbar;
    public ImageView trailerVideoImage, trailerVideoPlayImage;
    NestedScrollView pageContent;
    RecyclerView recyclerView;
    MovieCastAdapter adapter;
    private FragmentActivity mContext;

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

        pageContent = view.findViewById(R.id.movieContent);

        toolbar = view.findViewById(R.id.movieDialogTool_Bar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        recyclerView = view.findViewById(R.id.cast_recycler_view);

        trailerVideoImage = view.findViewById(R.id.movieTrailerImage);
        trailerVideoPlayImage = view.findViewById(R.id.movieTrailerPlayIcon);

        this.id = getArguments().getString("id", "No Title Found");

        getMovieDetails(view);

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

    protected synchronized void getMovieDetails(final View view) {
        TmdbClient.getMovieInfo(id, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                movieDetails = new Gson().fromJson(response.toString(), FullMovieDetails.class);
                if (movieDetails == null)
                    return;
                Uri uri = Uri.parse("https://image.tmdb.org/t/p/w1280" + movieDetails.getBackdrop_path());

                Glide.with(MovieFullScreenDialog.this).load(uri).listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        trailerVideoPlayImage.setVisibility(View.VISIBLE);

                        final FullMovieDetails.Video tempVideo = movieDetails.getVideos().get(0);

                        trailerVideoPlayImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openYoutubeVideo(getContext(), tempVideo.getKey());
                            }
                        });

                        view.findViewById(R.id.movieLoadingSpinner).setVisibility(View.GONE);
                        pageContent.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).dontAnimate().into(trailerVideoImage);

                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                TextView movieTitle = view.findViewById(R.id.movieTitle);
                TextView moviePlot = view.findViewById(R.id.movieInfoPlot);
                TextView movieReleaseDate = view.findViewById(R.id.movieInfoReleaseDate);
                TextView movieRuntime = view.findViewById(R.id.movieInfoRuntime);
                TextView movieGenres = view.findViewById(R.id.movieInfoGenres);
                Button movieCastMore = view.findViewById(R.id.movieInfoCastMore);


                String releaseDateString = getResources().getString(R.string.release_date) + " <font color='#ffffff'>" + movieDetails.getRelease_date() + "</font>";

                int runtimeMins = movieDetails.getRuntime();
                int hours = runtimeMins / 60, minutes = runtimeMins % 60;
                String runtimeString = String.format("%s %s", getResources().getString(R.string.runtime), String.format(" <font color='#ffffff'>%s</font>", String.format("%dhrs %02dmins", hours, minutes)));

                movieTitle.setText(movieDetails.getTitle());
                moviePlot.setText(movieDetails.getOverview());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    movieReleaseDate.setText(Html.fromHtml(releaseDateString, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
                    movieRuntime.setText(Html.fromHtml(runtimeString, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
                } else {
                    movieReleaseDate.setText(Html.fromHtml(releaseDateString), TextView.BufferType.SPANNABLE);
                    movieRuntime.setText(Html.fromHtml(runtimeString), TextView.BufferType.SPANNABLE);
                }

                movieGenres.setText(movieDetails.getGenresString());
                addCastToLayout(movieDetails.getCast(), getActivity().getSupportFragmentManager());
                movieCastMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewMoreCast();
                    }
                });
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(String.valueOf(statusCode), throwable.getMessage());
            }
        });
    }

    public void openYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(webIntent);
        }
    }

    public void addCastToLayout(ArrayList<FullMovieDetails.Cast> castList, FragmentManager fragmentManager) {
        Context mContext = getContext();
        List<FullMovieDetails.Cast> top3Cast = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            top3Cast.add(castList.get(i));
        }

        adapter = new MovieCastAdapter(mContext, top3Cast, fragmentManager);
        recyclerView.setAdapter(adapter);
    }

    public void viewMoreCast() {
        //TODO: Show popup of viewing more cast with the ability to click each one for their summary.
    }
}
