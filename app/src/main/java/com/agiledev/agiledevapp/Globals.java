package com.agiledev.agiledevapp;

import android.util.SparseArray;

public class Globals {
    private static SparseArray<String> genreTags = new SparseArray<>();

    public static SparseArray<String> getGenreTags() {
        return genreTags;
    }

    public static void setGenreTags(SparseArray<String> genres) {
        genreTags = genres;
    }

    private static LoginRegisterActivity.User currentUser;

    public static LoginRegisterActivity.User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(LoginRegisterActivity.User currentUser) {
        Globals.currentUser = currentUser;
    }
}
