package com.agiledev.agiledevapp;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by t7037453 on 29/01/19.
 */

public class HideKeyboard {
    public static void HideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if(view != null)
        {
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }
}
