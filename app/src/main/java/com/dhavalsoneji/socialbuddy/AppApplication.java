package com.dhavalsoneji.socialbuddy;

import android.app.Application;
import android.content.Context;

import com.dhavalsoneji.socialbuddy.utils.AppConstants;
import com.kittyapplication.core.CoreApp;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Dhaval Soneji Riontech on 6/12/16.
 */

public class AppApplication extends CoreApp {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(AppConstants.TWITTER_KEY,
                AppConstants.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
    }

    public static Context getContext() {
        return sContext;
    }

    public static AppApplication getInstance() {
        return (AppApplication) sContext;
    }
}
