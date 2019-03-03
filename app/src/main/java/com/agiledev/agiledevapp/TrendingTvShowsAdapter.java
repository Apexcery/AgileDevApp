package com.agiledev.agiledevapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

public class TrendingTvShowsAdapter extends RecyclerView.Adapter<TrendingTvShowsAdapter.MyViewHolder> {

    private Context mContext;
    private List<FullTvShowDetails> tvshowList;
    public FragmentManager manager;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView poster;
        String id;
        TrendingTvShowsAdapter adapter;
        RelativeLayout layout;
        String rating;

        MyViewHolder(View view) {
            super(view);


            poster = view.findViewById(R.id.trendingtvimg);
            //rating = view.findViewById(R.id.trendingtvrating);
            layout = view.findViewById(R.id.trendingtvlayout);

        }
    }

    TrendingTvShowsAdapter(Context mContext, List<FullTvShowDetails> tvshowList, FragmentManager manager) {
        this.mContext = mContext;
        this.tvshowList = tvshowList;
        this.manager = manager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_tvshowimage, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)  {
        FullTvShowDetails tvshow = tvshowList.get(position);
        holder.id = tvshow.getId();
        holder.rating = tvshow.getVote_average();
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TvShowFullScreenDialog dialog = TvShowFullScreenDialog.newInstance(holder.id);
                dialog.show(manager, TvShowFullScreenDialog.TAG);
            }
        });
        //TODO create a Tv show fullscreen dialog
        TmdbClient.loadImage(mContext, tvshow.getPoster_path(), holder.poster);

    }

    @Override
    public int getItemCount() {
        return tvshowList.size();
    }
}
