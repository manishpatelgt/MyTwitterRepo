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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mytwitter.Models.MyTweet;
import com.mytwitter.Models.ResponeWrap;
import com.mytwitter.Models.Response;
import com.mytwitter.Models.answers;
import com.mytwitter.Models.testBean;
import com.mytwitter.R;
import com.mytwitter.application.TwitterApplication;
import com.mytwitter.data.PreferencesHelper;
import com.mytwitter.retrofit.RetrofitClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Manish on 3/19/2016.
 */
public class HomeActivity extends AppCompatActivity {

    private static Logger logger = LoggerFactory.getLogger("c*.m*.MyTweetsAct*");
    private LinearLayout myLayout;
    private List<Long> tweetIds;
    private ProgressDialog mProgressDialog;
    private Menu menu;
    private Tracker mTracker;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_action_twitter);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        //toolbar.setDisplayUseLogoEnabled(true);
        //toolbar.setDisplayShowHomeEnabled(true);

        // Obtain the shared Tracker instance.
        TwitterApplication application = (TwitterApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // Set screen name.
        mTracker.setScreenName("HomeActivity");

        // Send a screen view.
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        myLayout = (LinearLayout) findViewById(R.id.my_tweet_layout);
        tweetIds = new ArrayList<Long>();

        mProgressDialog = new ProgressDialog(HomeActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //https://twittercommunity.com/t/how-to-get-user-hometimeline/52826 very useful
        StatusesService statusesService  = Twitter.getApiClient().getStatusesService();

        statusesService.homeTimeline(30, null, null, null, null, null, null,
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

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                Intent intent = new ComposerActivity.Builder(HomeActivity.this)
                        .session(session)
                        .createIntent();
                startActivity(intent);
            }
        });

        /*Call<List<MyTweet>> cb = statusesService.homeTimeline(10,0,0,false,false,false,false,Callback<List<MyTweet>>*/
        /*ArrayList<answers> _list=new ArrayList<answers>();
        _list.add(new answers(0,2,144));
        _list.add(new answers(1,2,50));
        _list.add(new answers(2,3,14));
        _list.add(new answers(3,6,152));
        _list.add(new answers(4,7,10));

        Gson gson = new Gson();

        String listString = gson.toJson(
                _list,
                new TypeToken<ArrayList<answers>>() {}.getType());

        try{

            Gson gson = new GsonBuilder().create();
            JsonArray jsonArray = gson.toJsonTree(_list).getAsJsonArray();
            System.out.println("jsonArray: "+jsonArray);
            //JSONArray jsonArray =  new JSONArray(listString);
            HashMap<String,String> searchFilters = new HashMap<>();
            searchFilters.put("answers",jsonArray.toString());

            Call<testBean> cb= RetrofitClient.StackyServices().loadtest(searchFilters);

            cb.enqueue(new retrofit2.Callback<testBean>() {

                @Override
                public void onResponse(Call<testBean> call, retrofit2.Response<testBean> response) {

                    if (mProgressDialog.isShowing() && mProgressDialog != null)
                        mProgressDialog.dismiss();

                    int statusCode = response.code();
                    if (response.isSuccessful()) {

                        testBean finalRes = response.body();
                        //logger.debug("status: "+finalRes.getResponse().getstatus());

                    } else {
                        // handle request errors yourself
                        ResponseBody errorBody = response.errorBody();
                    }
                }

                @Override
                public void onFailure(Call<testBean> call, Throwable t) {
                    // handle execution failures like no internet connectivity
                    logger.debug("onFailure");
                    t.printStackTrace();
                    if (mProgressDialog.isShowing() && mProgressDialog != null)
                        mProgressDialog.dismiss();
                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }*/

        /*Call<List<MyTweet>> cb= RetrofitClient.myHomeTweetsServices().getHomeTweetIds();

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
                        tweetIds.add(Long.valueOf(id.getId_str()).longValue());
                        //if (!id.getRetweeted()) {
                         //   tweetIds.add(Long.valueOf(id.getId_str()).longValue());
                       // }
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

                    TweetView tweetView = new TweetView(HomeActivity.this, result.data,
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //this.menu = menu;
        //MenuItem bedMenuItem = menu.findItem(R.id.action_profile);
        //bedMenuItem.setTitle(getString(R.string.action_profile)+" "+PreferencesHelper.getUserName());
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_timeline) {

            Intent i=new Intent(HomeActivity.this,MyTweetsActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_activity_in_right, R.anim.slide_activity_out_right);
            return true;

        }
        else if (id == R.id.action_retweet) {

            Intent i=new Intent(HomeActivity.this,MyReTweetsActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_activity_in_right, R.anim.slide_activity_out_right);
            return true;

        }
        else if (id == R.id.action_profile) {

            Intent i=new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_activity_in_right, R.anim.slide_activity_out_right);
            return true;

        }
        else if (id == R.id.action_lic) {

            displayLicensesDialogFragment();
            return true;

        }
        /*else if (id == R.id.action_tweet) {

             TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
             Intent intent = new ComposerActivity.Builder(HomeActivity.this)
                    .session(session)
                    .createIntent();
            startActivity(intent);
            return true;
        }*/
        else if (id == R.id.action_logout) {

            PreferencesHelper.setLoginCheck(false);
            Twitter.getSessionManager().clearActiveSession();
            Twitter.logOut();
            Intent i=new Intent(HomeActivity.this,LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
            return true;
        }
        if (id == R.id.action_aboutus) {

            Intent i=new Intent(HomeActivity.this,AboutUsActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_activity_in_right, R.anim.slide_activity_out_right);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void displayLicensesDialogFragment() {
        LicensesDialogFragment dialog = LicensesDialogFragment.newInstance();
        dialog.show(getSupportFragmentManager(), "LicensesDialog");
    }
}
