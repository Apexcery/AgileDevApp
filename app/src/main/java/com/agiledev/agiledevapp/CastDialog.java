package com.agiledev.agiledevapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by glees on 20/02/2019.
 */

public class CastDialog extends DialogFragment {
    public static String TAG = "CastDialog";
    public static MovieCastAdapter.Person cast;
    public Toolbar toolbar;
    RelativeLayout pageContent;

    public static CastDialog newInstance(MovieCastAdapter.Person person) {
        CastDialog fragment = new CastDialog();
        Bundle args = new Bundle();
        cast = person;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.cast_dialog_layout, container, false);

        pageContent = view.findViewById(R.id.castContent);

        toolbar = view.findViewById(R.id.castDialogTool_Bar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        displayCastDetails(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    protected synchronized void displayCastDetails(View view) {
        TextView toolbarTitle = view.findViewById(R.id.castName);
        ImageView personImage = view.findViewById(R.id.castPersonImage);
        TextView personName = view.findViewById(R.id.castPersonName);
        TextView personGender = view.findViewById(R.id.castPersonGender);
        TextView personKnownFor = view.findViewById(R.id.castPersonKnownFor);
        TextView personDOB = view.findViewById(R.id.castPersonDOB);
        TextView personDied = view.findViewById(R.id.castPersonDied);
        TextView personBio = view.findViewById(R.id.castPersonBio);

        toolbarTitle.setText(cast.getName());

        TmdbClient.loadImage(getContext(), cast.getProfile_path(), personImage, TmdbClient.imageType.ICON);

        SpannableString name = new SpannableString(cast.getName());
        name.setSpan(new UnderlineSpan(), 0, name.length(), 0);
        personName.setText(name);

        personGender.setText(cast.getGender() == 1 ? "Female" : "Male");

        String knownForText = "Known For - " + cast.getKnown_for_department();
        personKnownFor.setText(knownForText);

        String bornText = "Born - " + cast.getBirthday();
        personDOB.setText(bornText);

        if (cast.getDeathday() != null) {
            String diedText = "Died - " + cast.getDeathday();
            personDied.setText(diedText);
        }

        personBio.setText(cast.getBiography());
    }
}
