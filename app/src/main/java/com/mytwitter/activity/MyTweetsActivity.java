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
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.Tracker;
import com.mytwitter.Models.MyTweet;
import com.mytwitter.R;
import com.mytwitter.Utils.Consts;
import com.mytwitter.application.TwitterApplication;
import com.mytwitter.data.PreferencesHelper;
import com.mytwitter.retrofit.RetrofitClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Manish on 3/19/2016.
 */
public class MyTweetsActivity extends AppCompatActivity {

    private static Logger logger = LoggerFactory.getLogger("c*.m*.MyTweetsAct*");
    private LinearLayout myLayout;
    private List<Long> tweetIds;
    private ProgressDialog mProgressDialog;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_action_twitter);
        myLayout = (LinearLayout) findViewById(R.id.my_tweet_layout);
        tweetIds = new ArrayList<Long>();

        // Obtain the shared Tracker instance.
        TwitterApplication application = (TwitterApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // Set screen name.
        mTracker.setScreenName("MyTweetsActivity");

        mProgressDialog = new ProgressDialog(MyTweetsActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        StatusesService statusesService  = Twitter.getApiClient().getStatusesService();

        statusesService.userTimeline(PreferencesHelper.getUserID(), null, 20, null, null, null, null,null,false,
                new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {

                        if (mProgressDialog.isShowing() && mProgressDialog != null)
                            mProgressDialog.dismiss();

                        List<Tweet> tweets = listResult.data;

                        for (int i = 0; i < tweets.size(); i++) {
                            Tweet id = tweets.get(i);
                            tweetIds.add(Long.valueOf(id.getId()).longValue());
                        }
                        loadAllTweets();
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Snackbar.make(myLayout, e.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

        /*Call<List<MyTweet>> cb= RetrofitClient.myTweetsServices().getMyTweetIds(PreferencesHelper.getUserID());

        cb.enqueue(new retrofit2.Callback<List<MyTweet>>() {

            @Override
            public void onResponse(Call<List<MyTweet>> call, retrofit2.Response<List<MyTweet>> response) {

                if (mProgressDialog.isShowing() && mProgressDialog != null)
                    mProgressDialog.dismiss();

                int statusCode = response.code();
                if (response.isSuccessful()) {
                    List<MyTweet> Ids = response.body();

                    for (int i = 0; i < Ids.size(); i++) {
                        MyTweet id = Ids.get(i);

                        if (!id.getRetweeted()) {
                            tweetIds.add(Long.valueOf(id.getId_str()).longValue());
                        }
                    }

                    loadAllTweets();
                } else {
                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                }
            }

            @Override
            public void onFailure(Call<List<MyTweet>> call, Throwable t) {
                // handle execution failures like no internet connectivity
                logger.debug("onFailure");
                t.printStackTrace();
                if (mProgressDialog.isShowing() && mProgressDialog != null)
                    mProgressDialog.dismiss();
            }
        });*/

    }

    private void loadAllTweets(){

        for(final Long tweetId: tweetIds){

            TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    //Tweet tweet = result.data;

                    TweetView tweetView = new TweetView(MyTweetsActivity.this, result.data,
                            R.style.tw__TweetLightWithActionsStyle);

                    View breakLine = getLayoutInflater().inflate(R.layout.tweet_view_layout, null);
                    tweetView.setOnActionCallback(actionCallback);
                    myLayout.addView(tweetView);
                    myLayout.addView(breakLine);
                }

                @Override
                public void failure(TwitterException exception) {
                    // Toast.makeText(...).show();
                }
            });

        }
    }


    // launch the login activity when a guest user tries to favorite a Tweet
    final Callback<Tweet> actionCallback = new Callback<Tweet>() {
        @Override
        public void success(Result<Tweet> result) {
            // Intentionally blank
        }

        @Override
        public void failure(TwitterException exception) {
            if (exception instanceof TwitterAuthException) {
                // launch custom login flow
                //startActivity(LoginActivity.class);
            }
        }
    };

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // Use left swipe when user presses hardware back button
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
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
        if(id == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

