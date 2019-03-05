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

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.MyViewHolder> {

    private Context mContext;
    private List<FullMovieDetails.Cast> castList;
    public FragmentManager manager;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView realName, charName, gender, DOB, died;
        ImageView image;
        String id;
        RelativeLayout layout;
        Person person;

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
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_movie_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)  {
        final FullMovieDetails.Cast cast = castList.get(position);

        holder.realName.setText(cast.getName());
        holder.charName.setText(cast.getCharacter());
        holder.gender.setText(cast.getGender() == 1 ? "Female" : "Male");

        TmdbClient.getPersonDetails(cast.getId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,  JSONObject response) {
                holder.person = new Gson().fromJson(response.toString(), Person.class);
                if (holder.person == null)
                    return;
                String DOBString = holder.DOB.getText().toString() + " " + holder.person.birthday;
                holder.DOB.setText(DOBString);
                if (holder.person.deathday != null) {
                    String diedString = "Died - " + holder.person.deathday;
                    holder.died.setText(diedString);
                }
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CastDialog dialog = CastDialog.newInstance(holder.person);
                        dialog.show(manager, CastDialog.TAG);
                    }
                });
            }
        });

        TmdbClient.loadImage(mContext, cast.getProfile_path(), holder.image, TmdbClient.imageType.ICON);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class Person {
        private String birthday;
        private String known_for_department;
        private String deathday;
        private String name;
        private int gender;
        private String biography;
        private String place_of_birth;
        private String profile_path;

        public String getBirthday() {
            return birthday;
        }
        public String getKnown_for_department() {
            return known_for_department;
        }
        public String getDeathday() {
            return deathday;
        }
        public String getName() {
            return name;
        }
        public int getGender() {
            return gender;
        }
        public String getBiography() {
            return biography;
        }
        public String getPlace_of_birth() {
            return place_of_birth;
        }
        public String getProfile_path() {
            return profile_path;
        }
    }
}
