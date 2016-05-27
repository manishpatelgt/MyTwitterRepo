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

package com.mytwitter.retrofit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytwitter.APIcall.RestInterface;
import com.mytwitter.Utils.Consts;
import com.mytwitter.Utils.TimeHelper;
import com.mytwitter.application.TwitterApplication;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Manish on 31/10/2015.
 */
public class RetrofitClient {

    /**
     * Customization
     */

    private static final Context context;
    //private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new RetrofitRequestInterceptor());
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    //private static final OkHttpClient client = httpClient.addInterceptor(interceptor).build();

    // JSON mapping requires proper time formatting
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat(TimeHelper.TIMESTAMP_FORMAT_ISO8601)
            .create();

    static {
        context = TwitterApplication.getContext();
    }

    /**
     * RestAdapters
     */
    /*private static final Retrofit.Builder commonBuilder = new Retrofit.Builder()
            .client(httpClient.addInterceptor(interceptor).build())
            .addConverterFactory(GsonConverterFactory.create());*/

    private static final Retrofit myTweetsAdapter = new Retrofit.Builder()
            .baseUrl(Consts.mainURL)
            .client(new OkHttpClient.Builder().addInterceptor(new RetrofitRequestInterceptor()).addInterceptor(interceptor).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final Retrofit myHomeTweetssAdapter = new Retrofit.Builder()
            .baseUrl(Consts.mainURL)
            .client(new OkHttpClient.Builder().addInterceptor(new RetrofitRequestInterceptor2()).addInterceptor(interceptor).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final Retrofit stackyAdapter = new Retrofit.Builder()
            .baseUrl("http://content.guardianapis.com/")
            .client(new OkHttpClient.Builder().addInterceptor(interceptor).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final Retrofit stackyAdapter2 = new Retrofit.Builder()
            .baseUrl("http://content.guardianapis.com/")
            .client(new OkHttpClient.Builder().addInterceptor(interceptor).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    /**
     * Web service definitions
     */

    private static final RestInterface MY_HOME_TWEETS_SERVICES = myHomeTweetssAdapter.create(RestInterface.class);

    public static RestInterface myHomeTweetsServices() {
        return MY_HOME_TWEETS_SERVICES;
    }

    private static final RestInterface MY_TWEETS_SERVICES = myTweetsAdapter.create(RestInterface.class);

    public static RestInterface myTweetsServices() {
        return MY_TWEETS_SERVICES;
    }

    private static final RestInterface STACKY_SERVICES = stackyAdapter.create(RestInterface.class);

    public static RestInterface StackyServices() {
        return STACKY_SERVICES;
    }

}
