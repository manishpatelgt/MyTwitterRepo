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

package com.mytwitter.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mytwitter.R;
import com.mytwitter.Utils.Consts;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Manish on 3/11/2016.
 */
public class TwitterApplication extends MultiDexApplication {

    private Tracker mTracker;
    private static final String LOG_TAG = "MyOwnTwitter";
    public static TwitterApplication REF_GAPPLICATION;
    // Needs to be volatile as another thread can see a half initialised instance.
    private volatile static TwitterApplication applicationInstance;


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(Consts.TWITTER_KEY, Consts.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());


        // Initialize the Application singleton
        applicationInstance = this;
        applicationContext = getApplicationContext();

        preferences = applicationContext.getSharedPreferences("AppPreferences", Activity.MODE_PRIVATE);
    }

    private static Context applicationContext = null;

    public static Context getContext() {
        return applicationContext;
    }

    public static void setContext(Context newContext) {
        applicationContext = newContext;
    }

    private static SharedPreferences preferences;

    public static SharedPreferences getPreferences() {
        return preferences;
    }
}
