package com.dhavalsoneji.socialbuddy.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dhavalsoneji.socialbuddy.utils.AppConstants;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

/**
 * Created by Dhaval Soneji Riontech on 7/12/16.
 */

public class ResultReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
            // success
            context.sendBroadcast(new Intent(AppConstants.BROADCAST_TWEET_COMPOSE_SUCCESS));
            final Long tweetId = intent.getLongExtra(TweetUploadService.EXTRA_TWEET_ID, 0);
        } else {
            // failure
            final Intent retryIntent = intent.getParcelableExtra(TweetUploadService.EXTRA_RETRY_INTENT);
        }
    }
}
