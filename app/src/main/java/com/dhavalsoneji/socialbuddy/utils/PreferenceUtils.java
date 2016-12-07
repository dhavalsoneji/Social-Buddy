package com.dhavalsoneji.socialbuddy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dhavalsoneji.socialbuddy.ui.activity.Index;
import com.google.gson.Gson;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by Dhaval Soneji Riontech on 6/12/16.
 */

public class PreferenceUtils {

    /**
     * Clear Data From Preference
     *
     * @param ctx
     */
    public static void clearPreference(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(AppConstants.PREFERENCE_NAME, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Insert string value in Shared Preferences
     *
     * @param context of application
     * @param value   to store in preferences
     * @param key     using which value is mapped
     * @return
     */
    public static boolean putStringInPreferences(final Context context,
                                                 final String value, final String key) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(AppConstants.PREFERENCE_NAME,
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    /**
     * Get Data from preferance
     *
     * @param context
     * @param defaultValue
     * @param key
     * @return
     */
    public static String getStringFromPreferences(final Context context,
                                                  final String defaultValue, final String key) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(AppConstants.PREFERENCE_NAME,
                        Context.MODE_PRIVATE);
        final String temp = sharedPreferences.getString(key, defaultValue);
        return temp;
    }

    /**
     * Insert booblean in preferance
     *
     * @param context
     * @param value
     * @param key
     * @return
     */
    public static boolean putBooleanInPreferences(final Context context,
                                                  final boolean value, final String key) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(AppConstants.PREFERENCE_NAME,
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return true;
    }

    /**
     * Get boolean from preferance
     *
     * @param context
     * @param defaultValue
     * @param key
     * @return
     */
    public static boolean getBooleanFromPreferences(final Context context,
                                                    final boolean defaultValue, final String key) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(AppConstants.PREFERENCE_NAME,
                        Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Insert integer value in preferences
     *
     * @param context
     * @param value
     * @param key
     * @return
     */
    public static boolean putIntegerInPreferences(final Context context,
                                                  final int value, final String key) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(AppConstants.PREFERENCE_NAME,
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
        return true;
    }

    /**
     * Return integer preference value
     *
     * @param context
     * @param defaultValue
     * @param key
     * @return
     */
    public static int getIntegerFromPreferences(final Context context,
                                                final int defaultValue, final String key) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(AppConstants.PREFERENCE_NAME,
                        Context.MODE_PRIVATE);
        final int temp = sharedPreferences.getInt(key, defaultValue);
        return temp;
    }

    public static void setTwitterLoginSessionData(Context context, TwitterSession data) {
        PreferenceUtils.putBooleanInPreferences(context, true, AppConstants.P_IS_LOGGEDIN);

        PreferenceUtils.putStringInPreferences(context, new Gson().toJson(data),
                AppConstants.P_TWITTER_LOGIN_SESSION_DATA);
    }

    public static TwitterSession getTwitterLoginSessionData(Context context) {
        return new Gson().fromJson(PreferenceUtils.getStringFromPreferences
                (context, "", AppConstants.P_TWITTER_LOGIN_SESSION_DATA), TwitterSession.class);
    }

    public static boolean isLoggedIn(Context context) {
        return PreferenceUtils.getBooleanFromPreferences(context, false,
                AppConstants.P_IS_LOGGEDIN);
    }

    public static void setEmailAddress(Context context, String email) {
        PreferenceUtils.putBooleanInPreferences(context, true, AppConstants.P_HAS_EMAIL_PERMISSION);
        PreferenceUtils.putStringInPreferences(context, email, AppConstants.P_EMAIL);
    }

    public static String getEmailAddress(Context context) {
        return PreferenceUtils.getStringFromPreferences(context, "", AppConstants.P_EMAIL);
    }

    public static boolean hasEmailPermited(Context context) {
        return PreferenceUtils.getBooleanFromPreferences(context, false,
                AppConstants.P_HAS_EMAIL_PERMISSION);
    }
}
