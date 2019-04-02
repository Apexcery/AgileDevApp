package com.agiledev.agiledevapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Locale;

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
                                if (imgExt != null && !imgExt.isEmpty() && imageUri == null) {
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


        return view;
    }
}
