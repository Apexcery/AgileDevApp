package com.agiledev.agiledevapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference avatarRef = FirebaseStorage.getInstance().getReference().child("avatars");

    String imgExt;
    static Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPref = getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        getActivity().setTitle("Profile");

        TextView txtUsername = view.findViewById(R.id.profile_username);
        final TextView txtJoined = view.findViewById(R.id.profile_joined);
        TextView txtNoMoviesWatched = view.findViewById(R.id.profile_num_movies_watched);
        TextView txtNoTVShowsWatched = view.findViewById(R.id.profile_num_shows_watched);
        final CircleImageView imgAvatar = view.findViewById(R.id.profile_avatar);
        final TextView txtTimeWatched = view.findViewById(R.id.profile_time_watched);
        RecyclerView rcyLastMovies = view.findViewById(R.id.profile_last_movies_recycler);
        RecyclerView rcyLastShows = view.findViewById(R.id.profile_last_shows_recycler);

        txtUsername.setText(sharedPref.getString(getString(R.string.prefs_loggedin_username), "Not Logged In"));

        if (imageUri != null) {
            Glide.with(getActivity()).load(imageUri).placeholder(R.drawable.placeholder_med_cast).dontAnimate().into(imgAvatar);
        }

        db.collection("UserDetails").document(sharedPref.getString(getString(R.string.prefs_loggedin_username), "Not Logged In"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                DateFormat sdf = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                                String joinDate = sdf.format(((Timestamp)doc.get("join_date")).toDate());
                                txtJoined.setText(joinDate);

                                imgExt = doc.getString("avatarExt");
                                if (imgExt != null && !imgExt.isEmpty()) {
                                    avatarRef.child(sharedPref.getString(getString(R.string.prefs_loggedin_username), null) + imgExt).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageUri = uri;
                                            Glide.with(getActivity()).load(uri).placeholder(R.drawable.placeholder_med_cast).dontAnimate().into(imgAvatar);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Error", "-- Image Not Found --");
                                        }
                                    });
                                }
                                long minsWatched = ((Number)doc.get("timeWatched")).longValue();

                                long days = TimeUnit.MINUTES.toDays(minsWatched);
                                    minsWatched -= TimeUnit.DAYS.toMinutes(days);

                                long hours = TimeUnit.MINUTES.toHours(minsWatched);
                                    minsWatched -= TimeUnit.HOURS.toMinutes(hours);

                                long minutes = TimeUnit.MINUTES.toMinutes(minsWatched);

                                txtTimeWatched.setText(days + " Days | " + hours + " Hours | " + minutes + " Minutes");
                            } else {
                                Log.e("Profile", "Document not found");
                            }
                        } else {
                            Log.e("Profile", task.getException().getMessage());
                        }
                    }
                });

        int noMovies = Globals.getTrackedMovies().size();
        int noShows = Globals.getTrackedTvShows().size();
        txtNoMoviesWatched.setText(String.valueOf(noMovies));
        txtNoTVShowsWatched.setText(String.valueOf(noShows));

        populateLastWatched(rcyLastMovies, rcyLastShows);

        return view;
    }

    void populateLastWatched(RecyclerView movieRecycler, RecyclerView tvRecycler) {
        List<Globals.trackedMovie> lastMovies = Globals.getTrackedMovies();
        List<Globals.trackedTV> lastShows = Globals.getTrackedTvShows();

        RecyclerView.LayoutManager movieLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        movieRecycler.setLayoutManager(movieLayoutManager);
        RecyclerView.LayoutManager tvLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tvRecycler.setLayoutManager(tvLayoutManager);

        RecyclerView.Adapter adapter = new HorizontalAdapter(getContext(), lastMovies, getActivity().getSupportFragmentManager(), HorizontalAdapter.MediaType.MOVIE);
        movieRecycler.setAdapter(adapter);
        adapter = new HorizontalAdapter(getContext(), lastShows, getActivity().getSupportFragmentManager(), HorizontalAdapter.MediaType.TV);
        tvRecycler.setAdapter(adapter);
    }
}
