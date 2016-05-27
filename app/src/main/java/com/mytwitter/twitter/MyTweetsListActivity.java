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

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.mytwitter.Models.MyTweet;
import com.mytwitter.Models.TweetsIds;
import com.mytwitter.R;
import com.mytwitter.Utils.Utils;
import com.mytwitter.activity.LoginActivity;
import com.mytwitter.data.PreferencesHelper;
import com.mytwitter.retrofit.RetrofitClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetViewFetchAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * Created by Manish on 3/18/2016.
 */
public class MyTweetsListActivity extends AppCompatActivity {

    private static Logger logger = LoggerFactory.getLogger("c*.m*.MyTweetsListAct*");
    //use id_str as TweetId
    //private  TweetViewFetchAdapter adapter;
    private ProgressDialog mProgressDialog;
    private List<Long> tweetIds;
    private ListView list;
    //List<Long> tweetIds = Arrays.asList(710683837961887744L, 710543894992130048L, 710488153644265474L, 710480435910975489L, 710445491146858496L);
    private TweetViewFetchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tweetlist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        tweetIds = new ArrayList<Long>();
        list = (ListView)findViewById(R.id.list);

        mProgressDialog = new ProgressDialog(MyTweetsListActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        Call<List<MyTweet>> cb=RetrofitClient.myTweetsServices().getMyTweetIds(PreferencesHelper.getUserID());

        cb.enqueue(new retrofit2.Callback<List<MyTweet>>() {

            @Override
            public void onResponse(Call<List<MyTweet>> call, retrofit2.Response<List<MyTweet>> response) {

                if (mProgressDialog.isShowing() && mProgressDialog != null)
                    mProgressDialog.dismiss();

                int statusCode = response.code();
                logger.debug("statusCode: " + statusCode);
                logger.debug("Successful(): " + response.isSuccessful());

                // response.isSuccessful() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    List<MyTweet> Ids = response.body();
                    logger.debug("Total TweetIds: " + Ids.size());

                    for (int i = 0; i < Ids.size(); i++) {
                        MyTweet id = Ids.get(i);
                        //logger.debug("Retweet: "+id.getRetweeted());

                        if (!id.getRetweeted()) {
                            logger.debug("Id: " + id.getId_str());
                            tweetIds.add(Long.valueOf(id.getId_str()).longValue());
                            logger.debug("Id size: " + tweetIds.size());
                        }
                    }

                    logger.debug("tweetIds: " + tweetIds);
                    logger.debug("Id size2: " + tweetIds.size());

                    adapter = new TweetViewFetchAdapter<CompactTweetView>(MyTweetsListActivity.this);
                    //setListAdapter(adapter);
                    //adapter = new TweetViewFetchAdapter<CompactTweetView>(MyTweetsListActivity.this);
                    adapter.setTweetIds(tweetIds, new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> result) {
                            // my custom actions
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            // Toast.makeText(...).show();
                        }
                    });


                    list.setAdapter(adapter);
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            PreferencesHelper.setLoginCheck(false);
            Twitter.getSessionManager().clearActiveSession();
            Twitter.logOut();
            Intent i=new Intent(MyTweetsListActivity.this,LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
