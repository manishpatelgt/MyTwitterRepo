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

package com.mytwitter.twitter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.mytwitter.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

public class EmbeddedTweetsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embedded_tweets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LinearLayout myLayout = (LinearLayout) findViewById(R.id.my_tweet_layout);

        TweetUtils.loadTweet(710543894992130048L, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                //Tweet tweet = result.data;
                TweetView tweetView = new TweetView(EmbeddedTweetsActivity.this, result.data,
                        R.style.tw__TweetLightWithActionsStyle);

                tweetView.setOnActionCallback(actionCallback);
                myLayout.addView(tweetView);
            }

            @Override
            public void failure(TwitterException exception) {
                // Toast.makeText(...).show();
            }
        });

        /*TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
       // Can also use Twitter directly: Twitter.getApiClient()
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.show(710543894992130048L, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                //Do something with result, which provides a Tweet inside of result.data
                    Tweet tweet = result.data;
                    TweetView tweetView = new TweetView(EmbeddedTweetsActivity.this, result.data,
                        R.style.tw__TweetDarkWithActionsStyle);

                   tweetView.setOnActionCallback(actionCallback);
                   myLayout.addView(tweetView);

            }

            public void failure(TwitterException exception) {
                //Do something on failure
            }
        });*/

        /*final LinearLayout myLayout = (LinearLayout) findViewById(R.id.my_tweet_layout);

        final List<Long> tweetIds = Arrays.asList(510908133917487104L);
        TweetUtils.loadTweets(tweetIds, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                for (Tweet tweet : result.data) {
                    myLayout.addView(new TweetView(EmbeddedTweetsActivity.this, tweet));
                }
            }

            @Override
            public void failure(TwitterException exception) {
                // Toast.makeText(...).show();
            }
        });*/

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

}
