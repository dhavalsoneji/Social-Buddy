package com.dhavalsoneji.socialbuddy.utils;

import android.content.Context;
import android.widget.Toast;

import com.dhavalsoneji.socialbuddy.R;

/**
 * Created by Dhaval Soneji Riontech on 6/12/16.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static void showToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
    }

    public static void showToast(Context context) {
        try {
            Toast.makeText(context, context.getResources().getString(R.string.error_occured)
                    , Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
    }

    public static boolean isValidString(String message) {
        if (message.length() > 0 && !message.isEmpty() && message != null) {
            return true;
        } else {
            return false;
        }
    }
}
