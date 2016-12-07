package com.dhavalsoneji.socialbuddy.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.dhavalsoneji.socialbuddy.ui.activity.Index;
import com.dhavalsoneji.socialbuddy.utils.Utils;
import com.google.gson.Gson;
import com.kittyapplication.core.utils.Applog;
import com.kittyapplication.core.utils.DialogUtils;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * Created by Dhaval Soneji Riontech on 7/12/16.
 */
public class UserTimelineAdapter extends TweetTimelineListAdapter {
    private static final String TAG = UserTimelineAdapter.class.getSimpleName();
    private Context mContext;

    /**
     * Constructs a TweetTimelineListAdapter for the given Tweet Timeline.
     *
     * @param context  the context for row views.
     * @param timeline a Timeline&lt;Tweet&gt; providing access to Tweet data items.
     * @throws IllegalArgumentException if timeline is null
     */
    public UserTimelineAdapter(Context context, Timeline<Tweet> timeline) {
        super(context, timeline);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        try {
            //disable subviews to avoid links are clickable
            if (view instanceof ViewGroup) {
                disableViewAndSubViews((ViewGroup) view);
            }

            //enable root view and attach custom listener
            view.setEnabled(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.createTweetDialog(mContext,getItem(position).getId()).show();
                }
            });
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return view;
    }

    //helper method to disable subviews
    private void disableViewAndSubViews(ViewGroup layout) {
        try {
            layout.setEnabled(false);
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                if (child instanceof ViewGroup) {
                    disableViewAndSubViews((ViewGroup) child);
                } else {
                    child.setEnabled(false);
                    child.setClickable(false);
                    child.setLongClickable(false);
                }
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
    }
}
