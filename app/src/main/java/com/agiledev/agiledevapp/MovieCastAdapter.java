package com.agiledev.agiledevapp;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.MyViewHolder> {

    private Context mContext;
    private List<FullMovieDetails.Cast> castList;
    public FragmentManager manager;
    private Resources res;
    public Person person;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView realName, charName, gender, DOB, died;
        ImageView image;
        String id;
        RelativeLayout layout;

        MyViewHolder(View view) {
            super(view);
            realName = view.findViewById(R.id.movieCastCardName);
            charName = view.findViewById(R.id.movieCastCardCharacter);
            gender = view.findViewById(R.id.movieCastCardGender);
            image = view.findViewById(R.id.movieCastCardImage);
            DOB = view.findViewById(R.id.movieCastCardDOB);
            died = view.findViewById(R.id.movieCastCardDied);
            layout = view.findViewById(R.id.movieCastCard);
        }
    }

    MovieCastAdapter(Context mContext, List<FullMovieDetails.Cast> castList, FragmentManager manager) {
        this.mContext = mContext;
        this.castList = castList;
        this.manager = manager;
        this.res = mContext.getResources();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_movie_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)  {
        FullMovieDetails.Cast cast = castList.get(position);

        holder.realName.setText(cast.getName());
        holder.charName.setText(cast.getCharacter());
        holder.gender.setText(cast.getGender() == 1 ? "Female" : "Male");

        TmdbRestClient.get("person/" + cast.getId() + "?api_key=" + res.getString(R.string.tmdb_api_key), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,  JSONObject response) {
                person = new Gson().fromJson(response.toString(), Person.class);
                if (person == null)
                    return;
                String DOBString = holder.DOB.getText().toString() + " " + person.birthday;
                holder.DOB.setText(DOBString);
                if (person.deathday != null) {
                    String diedString = "Died - " + person.deathday;
                    holder.died.setText(diedString);
                }
            }
        });

        Glide.with(mContext).load(mContext.getResources().getString(R.string.movie_poster_icon_base_url) + cast.getProfile_path()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class Person {
        String birthday;
        String known_for_department;
        String deathday;
        String name;
        String biography;
        String place_of_birth;
    }
}
