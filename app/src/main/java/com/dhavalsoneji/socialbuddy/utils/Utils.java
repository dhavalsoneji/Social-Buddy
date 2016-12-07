package com.dhavalsoneji.socialbuddy.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dhavalsoneji.socialbuddy.R;
import com.dhavalsoneji.socialbuddy.ui.activity.Index;
import com.kittyapplication.core.utils.DialogUtils;
import com.kittyapplication.core.utils.Toaster;
import com.kittyapplication.core.utils.imagepick.ImagePickHelper;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetcomposer.Card;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

    public static Intent getComposeTweetIntent(Context context) {
        final Intent intent = new ComposerActivity.Builder(context)
                .session(PreferenceUtils.getTwitterLoginSessionData(context))
                .hashtags("#social-buddy")
                .createIntent();
        return intent;
    }

    public static Intent getComposeTweetIntent(Context context, Uri uri) {
        Applog.e(TAG, "" + uri.isAbsolute());

        Card card = new Card.AppCardBuilder(context)
                .imageUri(uri)
                .build();

        return new ComposerActivity.Builder(context)
                .session(PreferenceUtils.getTwitterLoginSessionData(context))
                .card(card)
                .hashtags("#social-buddy")
                .createIntent();
    }

    public static AlertDialog createTweetDialog(final Context context, long tweetId) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_tweet, null);
        final LinearLayout llTweet = (LinearLayout) view.findViewById(R.id.llTweet);
        view.findViewById(R.id.dialogProgressBar).setVisibility(View.VISIBLE);

        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                view.findViewById(R.id.dialogProgressBar).setVisibility(View.GONE);
                llTweet.addView(new TweetView(context, result.data,
                        R.style.tw__TweetLightWithActionsStyle));
            }

            @Override
            public void failure(TwitterException exception) {
                view.findViewById(R.id.dialogProgressBar).setVisibility(View.GONE);
                Applog.e(TAG, exception.getMessage(), exception);
                Toaster.longToast();
            }
        });

        dialogBuilder.setNegativeButton(R.string.close,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        dialogBuilder.setView(view);

        return dialogBuilder.create();
    }

    public static void logoutTwitter(final Activity activity) {
        try {

            if (PreferenceUtils.isLoggedIn(activity)) {
                CookieSyncManager.createInstance(activity);
                CookieManager cookieManager = CookieManager.getInstance();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.removeSessionCookies(new ValueCallback<Boolean>() {
                        @Override
                        public void onReceiveValue(Boolean aBoolean) {
                            Twitter.getSessionManager().clearActiveSession();
                            Twitter.logOut();
                            PreferenceUtils.clearPreference(activity);

                            Intent intent = activity.getIntent();
                            activity.finish();
                            activity.startActivity(intent);
                        }
                    });
                } else {
                    cookieManager.removeSessionCookie();
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                    PreferenceUtils.clearPreference(activity);

                    Intent intent = activity.getIntent();
                    activity.finish();
                    activity.startActivity(intent);
                }
            }

        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Toaster.longToast();
        }
    }

    public static boolean checkIsUriExist(Context context, Uri uri) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cur = cr.query(uri, projection, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                String filePath = cur.getString(0);

                if (new File(filePath).exists()) {
                    return true;
                } else {
                    // File was not found
                }
            } else {

                // Uri was ok but no entry found.
            }
            cur.close();
            return false;
        } else {
            return false;
            // content Uri was invalid or some other error occurred
        }
    }
}
