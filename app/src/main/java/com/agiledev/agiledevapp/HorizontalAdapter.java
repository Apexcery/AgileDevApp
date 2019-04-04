package com.agiledev.agiledevapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by t7097354 on 02/04/19.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private Context mContext;
    private List mediaList;
    private FragmentManager manager;
    private MediaType mediaType;

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        ImageView poster;

        MyViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.movieImageView);
            poster = view.findViewById(R.id.movieImageViewPoster);
        }
    }

    HorizontalAdapter(Context mContext, List mediaList, FragmentManager manager, MediaType mediaType) {
        this.mContext = mContext;
        this.mediaList = mediaList;
        this.manager = manager;
        this.mediaType = mediaType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_vertical_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (mediaType) {
            case MOVIE:
                final Globals.trackedMovie movie = (Globals.trackedMovie)mediaList.get(position);
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MovieFullScreenDialog dialog = MovieFullScreenDialog.newInstance(movie.id);
                        dialog.show(manager, MovieFullScreenDialog.TAG);
                    }
                });
                Glide.with(mContext).load(mContext.getResources().getString(R.string.poster_icon_base_url) + movie.poster_path).placeholder(R.drawable.placeholder_med_movie).override((int)(92 * mContext.getResources().getDisplayMetrics().density), (int)(154 * mContext.getResources().getDisplayMetrics().density)).dontAnimate().into(holder.poster);
                break;
            case TV:
                final Globals.trackedTV tv = (Globals.trackedTV)mediaList.get(position);
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TvShowFullScreenDialog dialog = TvShowFullScreenDialog.newInstance(tv.id);
                        dialog.show(manager, TvShowFullScreenDialog.TAG);
                    }
                });
                Glide.with(mContext).load(mContext.getResources().getString(R.string.poster_icon_base_url) + tv.poster_path).placeholder(R.drawable.placeholder_med_movie).override((int)(92 * mContext.getResources().getDisplayMetrics().density), (int)(154 * mContext.getResources().getDisplayMetrics().density)).dontAnimate().into(holder.poster);
                break;
        }
    }

    enum MediaType {
        MOVIE,
        TV
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }
}
