package com.agiledev.agiledevapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAvatarChangeDialog extends DialogFragment {
    public static String TAG = "ProfileAvatarChangeDialog";
    View view;

    static String username;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference avatarRef = FirebaseStorage.getInstance().getReference().child("avatars");

    public static ProfileAvatarChangeDialog newInstance(String name) {
        ProfileAvatarChangeDialog fragment = new ProfileAvatarChangeDialog();
        username = name;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.profile_avatar_change_layout, container, false);

        final CircleImageView imgAvatar = view.findViewById(R.id.avatar_change_image);
        final ProgressBar imgAvatarSpinner = view.findViewById(R.id.avatar_change_spinner);
        final Button btnAvatarChange = view.findViewById(R.id.avatar_change_button);

        final Button btnSkip = view.findViewById(R.id.avatar_change_skip);
        final Button btnConfirm = view.findViewById(R.id.avatar_change_confirm);

        btnAvatarChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAvatarChange();
            }
        });

        db.collection("UserDetails").document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                String imgExt = doc.getString("avatarExt");
                                if (imgExt != null && !imgExt.isEmpty()) {
                                    avatarRef.child(username + imgExt).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(getActivity()).load(uri).placeholder(R.drawable.placeholder_med_cast).dontAnimate().listener(new RequestListener<Uri, GlideDrawable>() {
                                                @Override
                                                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                    imgAvatarSpinner.setVisibility(View.GONE);
                                                    Toast.makeText(getContext(), "Avatar failed to load!", Toast.LENGTH_SHORT).show();
                                                    btnAvatarChange.setEnabled(true);
                                                    return false;
                                                }
                                                @Override
                                                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                    imgAvatarSpinner.setVisibility(View.GONE);
                                                    btnAvatarChange.setEnabled(true);
                                                    return false;
                                                }
                                            }).into(imgAvatar);
                                        }
                                    });
                                }
                            }
                        }
                    }
                });

        return view;
    }

    void changeAvatar() {
        //TODO: Implement this method.
    }

    void confirmAvatarChange() {
        //TODO: Implement this method.
    }
}
