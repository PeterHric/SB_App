package com.sbm.bc.smartbooksmobile;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by peter on 1.11.2016.
 */

public final class Utilities
{

    // A little utility method to test network availability
    public static boolean isNetworkConnected(Activity activity)
    {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    // Utility that hides the soft keyboard - but does not work somehow !
    private void hideSoftKeyboard(Activity activity)
    {
        int mysdk = Build.VERSION.SDK_INT;

        if (Build.VERSION.SDK_INT >= 14)
        {
            activity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
        }
        else if (Build.VERSION.SDK_INT >= 5)
        {
            if(activity.getCurrentFocus()!=null)
            {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow( activity.getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

}
