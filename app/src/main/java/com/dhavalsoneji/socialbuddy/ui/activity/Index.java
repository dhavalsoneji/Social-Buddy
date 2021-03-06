package com.dhavalsoneji.socialbuddy.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.dhavalsoneji.socialbuddy.R;
import com.dhavalsoneji.socialbuddy.ui.viewmodel.IndexViewModel;
import com.dhavalsoneji.socialbuddy.utils.AppConstants;
import com.dhavalsoneji.socialbuddy.utils.Applog;
import com.dhavalsoneji.socialbuddy.utils.Utils;
import com.kittyapplication.core.utils.imagepick.OnImagePickedListener;
import com.twitter.sdk.android.tweetcomposer.Card;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

public class Index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnImagePickedListener {

    private static final String TAG = Index.class.getSimpleName();
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private IndexViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_index);

        mViewModel = new IndexViewModel(this);

        mViewModel.initToolbar();
        mViewModel.initFabButton();
        mViewModel.initDrawer();
        mViewModel.initTwitter();
        registerReceiver(broadcastReceiver, new IntentFilter(AppConstants.BROADCAST_TWEET_COMPOSE_SUCCESS));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImagePicked(int requestCode, File file) {
        Uri uri = Uri.fromFile(file);
        startActivity(Utils.getComposeTweetIntent(Index.this, uri));
    }

    @Override
    public void onImagePickError(int requestCode, Exception e) {
        Applog.e(TAG, e.getMessage(), e);
    }

    @Override
    public void onImagePickClosed(int requestCode) {

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // internet lost alert dialog method call from here...
            mViewModel.refreshUserTimeline();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
