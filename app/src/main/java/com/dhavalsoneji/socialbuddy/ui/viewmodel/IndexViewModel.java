package com.dhavalsoneji.socialbuddy.ui.viewmodel;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dhavalsoneji.socialbuddy.R;
import com.dhavalsoneji.socialbuddy.ui.activity.Index;
import com.dhavalsoneji.socialbuddy.ui.adapter.UserTimelineAdapter;
import com.dhavalsoneji.socialbuddy.utils.AppConstants;
import com.dhavalsoneji.socialbuddy.utils.Applog;
import com.dhavalsoneji.socialbuddy.utils.PreferenceUtils;
import com.dhavalsoneji.socialbuddy.utils.Utils;
import com.kittyapplication.core.utils.DialogUtils;
import com.kittyapplication.core.utils.Toaster;
import com.kittyapplication.core.utils.imagepick.ImagePickHelper;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * Created by Dhaval Soneji Riontech on 6/12/16.
 */
public class IndexViewModel implements DialogInterface.OnClickListener {
    private static final String TAG = IndexViewModel.class.getSimpleName();
    private Index mActivity;
    private Toolbar mToolbar;
    private TwitterLoginButton mBtnTwitterLogin;
    private Button mBtnRequestEmail;
    private ListView mLvTweet;
    private Button mBtnTweet;
    private UserTimeline mUserTimeline;
    private ProgressBar mProgressBar;
    private UserTimelineAdapter mUserTimelineAdapter;
    private Callback<Tweet> actionCallback;
    private TweetTimelineListAdapter adapter;
    private Uri mImageUri;

    public IndexViewModel(Index activity) {
        mActivity = activity;
    }

    public void initToolbar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(mToolbar);
    }

    public void initFabButton() {
        try {
            FloatingActionButton fab = (FloatingActionButton) mActivity.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.logoutTwitter(mActivity);
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }
            });
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Toaster.longToast();
        }
    }

    public void initDrawer() {
        try {
            DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    mActivity, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) mActivity.findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(mActivity);
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Toaster.longToast();
        }
    }

    public void initTwitter() {
        try {
            mBtnTwitterLogin = (TwitterLoginButton) mActivity.findViewById(R.id.btnTwitterLogin);
            mBtnRequestEmail = (Button) mActivity.findViewById(R.id.btnRequestEmail);
            mLvTweet = (ListView) mActivity.findViewById(R.id.lvTweet);
            mBtnTweet = (Button) mActivity.findViewById(R.id.btnTweet);
            mProgressBar = (ProgressBar) mActivity.findViewById(R.id.pbListTweet);

            if (PreferenceUtils.isLoggedIn(mActivity)) {
                hideLoginButton(true);
            } else {
                hideLoginButton(false);
            }

            mBtnTwitterLogin.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    // Do something with result, which provides a TwitterSession for making API calls
                    PreferenceUtils.setTwitterLoginSessionData(mActivity, result.data);
                    mBtnTwitterLogin.setVisibility(View.GONE);
                    mBtnRequestEmail.setVisibility(View.VISIBLE);
                }

                @Override
                public void failure(TwitterException e) {
                    // Do something on failure
                    Applog.e(TAG, e.getMessage(), e);
                    Toaster.longToast();
                }
            });

            mBtnRequestEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TwitterAuthClient authClient = new TwitterAuthClient();
                    authClient.requestEmail(PreferenceUtils.getTwitterLoginSessionData(mActivity)
                            , new Callback<String>() {
                                @Override
                                public void success(Result<String> result) {
                                    // Do something with the result, which provides the email address
                                    PreferenceUtils.setEmailAddress(mActivity, result.data);
                                    hideEmailButton(true);
                                    Utils.showToast(mActivity, result.data);
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    // Do something on failure
                                    Applog.e(TAG, e.getMessage(), e);
                                    Toaster.longToast();
                                }
                            });
                }
            });

            mBtnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.startActivity(Utils.getComposeTweetIntent(mActivity));

                    /*DialogUtils.createDialog(mActivity,
                            R.string.upload_a_picture, R.string.upload_a_picture,
                            R.string.photo, R.string.skip, null,
                            positiveClickListener, negativeClickListener).show();*/
                }
            });

        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Toaster.longToast();
        }
    }

    private void hideEmailButton(boolean hasEmailPermitted) {
        try {
            if (hasEmailPermitted) {
                mBtnRequestEmail.setVisibility(View.GONE);
                mBtnTwitterLogin.setVisibility(View.GONE);
                mBtnTweet.setVisibility(View.VISIBLE);
                mLvTweet.setVisibility(View.VISIBLE);
                Utils.showToast(mActivity, PreferenceUtils.
                        getTwitterLoginSessionData(mActivity).getUserName());

                initUserTimeline();
            } else {
                mBtnRequestEmail.setVisibility(View.VISIBLE);
                mBtnTwitterLogin.setVisibility(View.GONE);
                mLvTweet.setVisibility(View.GONE);
                mBtnTweet.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Toaster.longToast();
        }
    }

    private void initUserTimeline() {
        showProgressBar();
        mUserTimeline = new UserTimeline.Builder()
                .screenName(PreferenceUtils.
                        getTwitterLoginSessionData(mActivity).getUserName())
                .maxItemsPerRequest(5)
                .build();

//        adapter = new TweetTimelineListAdapter.Builder(mActivity)
//                .setTimeline(mUserTimeline)
//                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
//                .setOnActionCallback(actionCallback)
//                .build();


        mUserTimelineAdapter = new UserTimelineAdapter(mActivity, mUserTimeline);
        mUserTimeline.next(null, new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                hideProgressBar();
            }

            @Override
            public void failure(TwitterException exception) {
                hideProgressBar();
                Applog.e(TAG, exception.getMessage(), exception);
                Toaster.longToast();
            }
        });
        mLvTweet.setAdapter(mUserTimelineAdapter);
    }

    private void hideLoginButton(boolean isLoggedIn) {
        try {
            if (isLoggedIn) {
                if (PreferenceUtils.hasEmailPermited(mActivity)) {
                    hideEmailButton(true);
                } else {
                    hideEmailButton(false);
                }
            } else {
                mBtnTwitterLogin.setVisibility(View.VISIBLE);
                mBtnRequestEmail.setVisibility(View.GONE);
                mLvTweet.setVisibility(View.GONE);
                mBtnTweet.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Toaster.longToast();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mBtnTwitterLogin.onActivityResult(requestCode, resultCode, data);
    }

    public void setImageUri(Uri imageUri) {
        mImageUri = imageUri;
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void refreshUserTimeline() {
        showProgressBar();
        mUserTimelineAdapter.refresh(new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                hideProgressBar();
            }

            @Override
            public void failure(TwitterException exception) {
                hideProgressBar();
                Toaster.longToast();
                Applog.e(TAG, exception.getMessage(), exception);
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    private DialogInterface.OnClickListener positiveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
            ImagePickHelper imagePickHelper = new ImagePickHelper();
            imagePickHelper.pickAnImage(mActivity, AppConstants.REQ_CODE_PICK_IMAGE);
        }
    };
    private DialogInterface.OnClickListener negativeClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
            mActivity.startActivity(Utils.getComposeTweetIntent(mActivity));
        }
    };
}
