package com.dhavalsoneji.socialbuddy.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.dhavalsoneji.socialbuddy.R;
import com.dhavalsoneji.socialbuddy.ui.activity.Index;
import com.dhavalsoneji.socialbuddy.utils.AppConstants;
import com.dhavalsoneji.socialbuddy.utils.Applog;
import com.dhavalsoneji.socialbuddy.utils.PreferenceUtils;
import com.dhavalsoneji.socialbuddy.utils.Utils;
import com.kittyapplication.core.utils.imagepick.ImagePickHelper;
import com.kittyapplication.core.utils.imagepick.OnImagePickedListener;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.Card;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import java.io.File;
import java.net.URI;

/**
 * Created by Dhaval Soneji Riontech on 6/12/16.
 */
public class IndexViewModel {
    private static final String TAG = IndexViewModel.class.getSimpleName();
    private Index mActivity;
    private Toolbar mToolbar;
    private TwitterLoginButton mTwitterLoginButton;
    private Button mEmailBtnResuest;
    private CardView tweetCardView;
    private ListView listTweet;
    private Button mBtnTweet;
    private Button mBtnPhoto;
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
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Utils.showToast(mActivity);
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
            Utils.showToast(mActivity);
        }
    }

    public void initTwitter() {
        try {
            mTwitterLoginButton = (TwitterLoginButton) mActivity.findViewById(R.id.btnTwitterLogin);
            mEmailBtnResuest = (Button) mActivity.findViewById(R.id.btnTwitterEmailReq);
            tweetCardView = (CardView) mActivity.findViewById(R.id.cardTweet);
            listTweet = (ListView) mActivity.findViewById(R.id.listTweet);
            mBtnTweet = (Button) mActivity.findViewById(R.id.btnTweet);
            mBtnPhoto = (Button) mActivity.findViewById(R.id.btnPhoto);

            if (PreferenceUtils.isLoggedIn(mActivity)) {
                hideLoginButton(true);
            } else {
                hideLoginButton(false);
            }

            mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    // Do something with result, which provides a TwitterSession for making API calls
                    PreferenceUtils.setTwitterLoginSessionData(mActivity, result.data);
                    mTwitterLoginButton.setVisibility(View.GONE);
                    mEmailBtnResuest.setVisibility(View.VISIBLE);
                }

                @Override
                public void failure(TwitterException e) {
                    // Do something on failure
                    Applog.e(TAG, e.getMessage(), e);
                    Utils.showToast(mActivity);
                }
            });

            mEmailBtnResuest.setOnClickListener(new View.OnClickListener() {
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
                                    Utils.showToast(mActivity);
                                }
                            });
                }
            });

            mBtnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utils.isValidString(((EditText) mActivity.findViewById(R.id.editMessage))
                            .getText().toString().trim())) {
                        if (mImageUri != null) {
                            Card card = new Card.AppCardBuilder(mActivity)
                                    .imageUri(mImageUri)
                                    .build();

                            final Intent intent = new ComposerActivity.Builder(mActivity)
                                    .session(PreferenceUtils.getTwitterLoginSessionData(mActivity))
//                                    .card(card)
                                    .hashtags("#social-buddy")
                                    .createIntent();

                            mActivity.startActivity(intent);
                        } else {
                            Utils.showToast(mActivity, mActivity.getResources().getString(R.string.select_image));
                        }
                    } else {
                        Utils.showToast(mActivity, mActivity.getResources().getString(R.string.write_text));
                    }
                }
            });

            mBtnPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePickHelper imagePickHelper = new ImagePickHelper();
                    imagePickHelper.pickAnImage(mActivity, AppConstants.REQ_CODE_PICK_IMAGE);
                }
            });
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Utils.showToast(mActivity);
        }
    }

    private void hideEmailButton(boolean hasEmailPermitted) {
        try {
            if (hasEmailPermitted) {
                mEmailBtnResuest.setVisibility(View.GONE);
                mTwitterLoginButton.setVisibility(View.GONE);
                tweetCardView.setVisibility(View.VISIBLE);
                listTweet.setVisibility(View.VISIBLE);
                Utils.showToast(mActivity, PreferenceUtils.
                        getTwitterLoginSessionData(mActivity).getUserName());
            } else {
                mEmailBtnResuest.setVisibility(View.VISIBLE);
                mTwitterLoginButton.setVisibility(View.GONE);
                tweetCardView.setVisibility(View.GONE);
                listTweet.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Utils.showToast(mActivity);
        }
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
                mTwitterLoginButton.setVisibility(View.VISIBLE);
                mEmailBtnResuest.setVisibility(View.GONE);
                tweetCardView.setVisibility(View.GONE);
                listTweet.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            Utils.showToast(mActivity);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void setImageUri(Uri imageUri) {
        mImageUri = imageUri;
    }
}
