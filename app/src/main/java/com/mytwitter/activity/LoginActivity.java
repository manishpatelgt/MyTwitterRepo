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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mytwitter.Network.NetworkHelper;
import com.mytwitter.Utils.Consts;
import com.mytwitter.R;
import com.mytwitter.Utils.Utils;
import com.mytwitter.application.TwitterApplication;
import com.mytwitter.data.PreferencesHelper;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginActivity extends AppCompatActivity {

    private static Logger logger = LoggerFactory.getLogger("c*.m*.LoginAct*");
    private static final int TWEET_COMPOSER_REQUEST_CODE = 10000;
    private TwitterLoginButton loginButton;
    int statusBarHeight;
    private TextView versionText;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.black_trans80));

            statusBarHeight = Utils.getStatusBarHeight(this);
        }

        setContentView(R.layout.activity_login);

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        versionText=(TextView)findViewById(R.id.version);
        System.out.println("version: " + Utils.getApplicationVersionName(this));

        versionText.setText("v" + Utils.getApplicationVersionName(this)+" | "+getResources().getString(R.string.release_notes));

        if(NetworkHelper.connectedToWiFiOrMobileNetwork(getApplicationContext())){

            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
                    // Do something with result, which provides a TwitterSession for making API calls

                    String output = "Status: " +
                            "Your login was successful " +
                            twitterSessionResult.data.getUserName() +
                            "\nAuth Token Received: " +
                            twitterSessionResult.data.getAuthToken().token+
                            "\nSecret: " +
                            twitterSessionResult.data.getAuthToken().secret+
                            "\nUserId "+
                            twitterSessionResult.data.getUserId();

                    logger.debug("done: " + output);

                    Consts.UserId =  twitterSessionResult.data.getUserId();
                    //Consts.Token =  twitterSessionResult.data.getAuthToken();
                    Consts.UserName =  twitterSessionResult.data.getUserName();

                    //Save Active Session

                    TwitterSession session = Twitter.getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = session.getAuthToken();
                    Consts.Token = authToken.token;
                    Consts.Secret = authToken.secret;

                    PreferencesHelper.setUserID(Consts.UserId);
                    PreferencesHelper.setUserName(Consts.UserName);
                    PreferencesHelper.setLoginCheck(true);
                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                    logger.debug("failed:");
                    //Toast.makeText(LoginActivity.this, "Authentication Failed!!! " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar.make(loginButton, exception.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

        }else{
            Snackbar.make(loginButton, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }


        // Obtain the shared Tracker instance.
        TwitterApplication application = (TwitterApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // Set screen name.
        mTracker.setScreenName("LoginActivity");

        // Send a screen view.
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        logger.debug("Result Code :: " + resultCode + " request code :: " + requestCode + " ");

        switch (requestCode) {
            case 140:
                switch (resultCode) {
                    case 0:
                        unauthorized();
                        return;
                    case -1:
                        authorized(requestCode, resultCode, data);
                        break;
                    case TWEET_COMPOSER_REQUEST_CODE:
                        //Toast.makeText(getApplicationContext(), "Posted done...", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        undefinedAccess(resultCode);
                }
                break;
        }
    }

    private void authorized(int requestCode, int resultCode, Intent data) {
        loginButton.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "Login Successfully.", Toast.LENGTH_LONG).show();

        Intent i=new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_activity_in_right, R.anim.slide_activity_out_right);

    }

    private void unauthorized() {
       // Toast.makeText(getApplicationContext(), "Authorize failed! Unable to get Login credential.", Toast.LENGTH_LONG).show();
        Snackbar.make(loginButton, "Authorize failed! Unable to get Login credential.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void undefinedAccess(int resultCode) {
       // Toast.makeText(getApplicationContext(), "Authorize failed! Undefined Access. Received result code :: " + resultCode, Toast.LENGTH_LONG).show();
        Snackbar.make(loginButton, "Authorize failed! Undefined Access. Received result code :: " + resultCode, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
