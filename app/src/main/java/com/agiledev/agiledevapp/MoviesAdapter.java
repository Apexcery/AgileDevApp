package com.agiledev.agiledevapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> movieList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, year;
        ImageView poster;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.movieCardTitle);
            year = view.findViewById(R.id.movieCardYear);
            poster = view.findViewById(R.id.movieCardPoster);
        }
    }

    MoviesAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_movie_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)  {
        Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(mContext.getString(R.string.movie_card_released, movie.getYear()));

        Glide.with(mContext).load(movie.getPoster()).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
