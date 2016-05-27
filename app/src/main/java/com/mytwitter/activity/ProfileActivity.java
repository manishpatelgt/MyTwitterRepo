/*
 *
 * Copyright 2016 Manish Patel (MD)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.mytwitter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.meg7.widget.*;
import com.mytwitter.APIcall.CustomService;
import com.mytwitter.R;
import com.mytwitter.Utils.Consts;
import com.mytwitter.application.TwitterApplication;
import com.mytwitter.data.PreferencesHelper;
import com.mytwitter.widgets.FontsHelper;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.mytwitter.APIcall.MyTwitterApiClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

/**
 * Created by Manish on 4/30/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    private static Logger logger = LoggerFactory.getLogger("c*.m*.ProfilesAct*");
    private ProgressDialog mProgressDialog;
    private ImageView banner_img;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mCollapsedTitle, mExpandedTitle;
    private ImageView profile_img;
    private TextView textUserName, textUserFollowers, textFollowing, textLikes;
    private Bitmap myBitmap;
    private LinearLayout rowContainer;
    private LayoutInflater layoutInflater;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_twitter);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Obtain the shared Tracker instance.
        TwitterApplication application = (TwitterApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // Set screen name.
        mTracker.setScreenName("ProfileActivity");

        // Send a screen view.
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        rowContainer = (LinearLayout) findViewById(R.id.main_layout);
        textUserName = (TextView) findViewById(R.id.userName);
        Typeface typeface = FontsHelper.loadFont(getApplicationContext(), FontsHelper.FONT_ROBOTO_REGULAR);
        textUserName.setTypeface(typeface);
        textUserFollowers = (TextView) findViewById(R.id.userFollowers);
        textUserFollowers.setTypeface(typeface);
        textLikes = (TextView) findViewById(R.id.userLikes);
        textLikes.setTypeface(typeface);

        textFollowing = (TextView) findViewById(R.id.userFollowing);
        textFollowing.setTypeface(typeface);
        layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        textUserName.setText("@" + PreferencesHelper.getUserName());
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        //appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        banner_img = (ImageView) findViewById(R.id.banner_img);
        profile_img = (ImageView) findViewById(R.id.profile_img);

        /*appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

                if(offset == 0){
                    toolbar.setTitle(mCollapsedTitle);
                }else{
                    toolbar.setTitle(mExpandedTitle);
                }
            }
        });*/

        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        TwitterSession session = Twitter.getSessionManager().getActiveSession();

        CustomService service = new MyTwitterApiClient(session).getCustomService();

        logger.debug("UserId: " + PreferencesHelper.getUserID());
        service.getUser(PreferencesHelper.getUserID(), new Callback<User>() {

            @Override
            public void success(Result<User> result) {

                if (mProgressDialog.isShowing() && mProgressDialog != null)
                    mProgressDialog.dismiss();

                User user = result.data;
                mCollapsedTitle = "" + user.name;
                collapsingToolbar.setTitle(mCollapsedTitle);
                getSupportActionBar().setSubtitle("@" + PreferencesHelper.getUserName());
                mExpandedTitle = "" + user.name;
                collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                logger.debug("Id: " + user.id);
                logger.debug("Name: " + user.name);
                logger.debug("description: " + user.description);
                logger.debug("email: " + user.email);
                String imageUrl = user.profileBannerUrl;
                Picasso.with(ProfileActivity.this).load(imageUrl).into(banner_img, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        try {

                            myBitmap = ((BitmapDrawable) banner_img.getDrawable()).getBitmap();

                            Palette palette = Palette.from(myBitmap).generate();

                           /*Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                           Palette.Swatch vibrantLightSwatch = palette.getLightVibrantSwatch();
                           Palette.Swatch vibrantDarkSwatch = palette.getDarkVibrantSwatch();
                           Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                           Palette.Swatch mutedLightSwatch = palette.getLightMutedSwatch();
                           Palette.Swatch mutedDarkSwatch = palette.getDarkMutedSwatch();*/

                            Palette.Swatch swatch = palette.getLightVibrantSwatch();
                            Palette.Swatch swatch2 = palette.getDarkVibrantSwatch();
                            /*int rgbColor = swatch.getRgb();
                            float[] hslValues = swatch.getHsl();*/
                            int titleTextColor = swatch.getTitleTextColor();
                            //int bodyTextColor = swatch.getBodyTextColor();

                            textUserFollowers.setTextColor(titleTextColor);
                            textFollowing.setTextColor(titleTextColor);
                            textUserName.setTextColor(titleTextColor);
                            textLikes.setTextColor(titleTextColor);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError() {
                    }
                });

                textUserFollowers.setText("Followers " + "\n" + user.followersCount);
                textFollowing.setText("Following " + "\n" + user.friendsCount);
                textLikes.setText("Favourites " + "\n" + user.favouritesCount);
                String imageUrl2 = user.profileImageUrl;
                Picasso.with(ProfileActivity.this).load(imageUrl2).into(profile_img, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });

                View descView = layoutInflater.inflate(R.layout.row_item, null);
                fillRow(descView,user.description);

                View tweetView = layoutInflater.inflate(R.layout.row_item4, null);
                fillRow4(tweetView,user.statusesCount);

                View locationView = layoutInflater.inflate(R.layout.row_item5, null);
                fillRow5(locationView,user.location);

                View listView = layoutInflater.inflate(R.layout.row_item3, null);
                fillRow3(listView,user.listedCount);

                View createdatView = layoutInflater.inflate(R.layout.row_item2, null);
                fillRow2(createdatView,user.createdAt);
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
                if (mProgressDialog.isShowing() && mProgressDialog != null)
                    mProgressDialog.dismiss();
                Snackbar.make(rowContainer, e.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void fillRow(View view, final String description) {

        TextView titleView = (TextView) view.findViewById(R.id.text_desc);
        titleView.setText(description);

        Typeface typeface = FontsHelper.loadFont(getApplicationContext(), FontsHelper.FONT_ROBOTO_REGULAR);
        titleView.setTypeface(typeface);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, Consts.Card_MarginTop, 0, Consts.Card_MarginBottom);
            rowContainer.addView(view, layoutParams);

        } else{
            rowContainer.addView(view);
        }

    }

    public void fillRow5(View view, final String location) {

        TextView titleView = (TextView) view.findViewById(R.id.text_location);
        titleView.setText("Location: "+location);

        Typeface typeface = FontsHelper.loadFont(getApplicationContext(), FontsHelper.FONT_ROBOTO_REGULAR);
        titleView.setTypeface(typeface);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, Consts.Card_MarginBottom);
            rowContainer.addView(view, layoutParams);

        } else{
            rowContainer.addView(view);
        }

    }

    public void fillRow4(View view, final int tweets) {

        TextView titleView = (TextView) view.findViewById(R.id.text_tweets);
        titleView.setText("Tweets: "+tweets);

        Typeface typeface = FontsHelper.loadFont(getApplicationContext(), FontsHelper.FONT_ROBOTO_REGULAR);
        titleView.setTypeface(typeface);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,0, Consts.Card_MarginBottom);
            rowContainer.addView(view, layoutParams);

        } else{
            rowContainer.addView(view);
        }

    }


    public void fillRow3(View view, final int text_list) {

         TextView titleView = (TextView) view.findViewById(R.id.text_memberof);
        titleView.setText("Member of: "+text_list+" list");

        Typeface typeface = FontsHelper.loadFont(getApplicationContext(), FontsHelper.FONT_ROBOTO_REGULAR);
        titleView.setTypeface(typeface);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,0, Consts.Card_MarginBottom);
            rowContainer.addView(view, layoutParams);

        } else{
            rowContainer.addView(view);
        }

    }

    public void fillRow2(View view, final String text_createat) {

        SimpleDateFormat sdf = new SimpleDateFormat("ddd MMM dd HH:mm:ss"); // the format of your date
        TextView titleView = (TextView) view.findViewById(R.id.text_createat);
        titleView.setText("Created at: "+text_createat);

        Typeface typeface = FontsHelper.loadFont(getApplicationContext(), FontsHelper.FONT_ROBOTO_REGULAR);
        titleView.setTypeface(typeface);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0, Consts.Card_MarginBottom);
                rowContainer.addView(view, layoutParams);

            } else{
                rowContainer.addView(view);
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Use left swipe when user presses hardware back button
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
    }

}
