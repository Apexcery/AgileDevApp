package com.agiledev.agiledevapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<BasicMovieDetails> movieList;
    public FragmentManager manager;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, year;
        ImageView poster;
        String imdbID;
        RelativeLayout layout;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.movieCardTitle);
            year = view.findViewById(R.id.movieCardYear);
            poster = view.findViewById(R.id.movieCardPoster);
            layout = view.findViewById(R.id.movieCard);
        }
    }

    MoviesAdapter(Context mContext, List<BasicMovieDetails> movieList, FragmentManager manager) {
        this.mContext = mContext;
        this.movieList = movieList;
        this.manager = manager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_movie_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)  {
        BasicMovieDetails movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(mContext.getString(R.string.movie_card_released, movie.getYear()));
        holder.imdbID = movie.getImdbID();
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieFullScreenDialog dialog = MovieFullScreenDialog.newInstance(holder.imdbID);
                dialog.show(manager, MovieFullScreenDialog.TAG);
            }
        });

        Glide.with(mContext).load(movie.getPoster()).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
