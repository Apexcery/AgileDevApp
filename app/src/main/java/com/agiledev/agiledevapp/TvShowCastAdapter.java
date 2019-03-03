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

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TvShowCastAdapter extends RecyclerView.Adapter<TvShowCastAdapter.MyViewHolder> {

    private Context mContext;
    private List<FullTvShowDetails.Cast> castList;
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
            realName = view.findViewById(R.id.tvshowCastCardName);
            charName = view.findViewById(R.id.tvshowCastCardCharacter);
            gender = view.findViewById(R.id.tvshowCastCardGender);
            image = view.findViewById(R.id.tvshowCastCardImage);
            DOB = view.findViewById(R.id.tvshowCastCardDOB);
            died = view.findViewById(R.id.tvshowCastCardDied);
            layout = view.findViewById(R.id.tvshowCastCard);
        }
    }

    TvShowCastAdapter(Context mContext, List<FullTvShowDetails.Cast> castList, FragmentManager manager) {
        this.mContext = mContext;
        this.castList = castList;
        this.manager = manager;
        this.res = mContext.getResources();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_tvshow_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)  {
        FullTvShowDetails.Cast cast = castList.get(position);

        holder.realName.setText(cast.getName());
        holder.charName.setText(cast.getCharacter());
        holder.gender.setText(cast.getGender() == 1 ? "Female" : "Male");

        TmdbClient.getPersonDetails(cast.getId(), null, new JsonHttpResponseHandler() {
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

        TmdbClient.loadImage(mContext, cast.getProfile_path(), holder.image);
//        Glide.with(mContext).load(mContext.getResources().getString(R.string.movie_poster_icon_base_url) + cast.getProfile_path()).into(holder.image);
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
