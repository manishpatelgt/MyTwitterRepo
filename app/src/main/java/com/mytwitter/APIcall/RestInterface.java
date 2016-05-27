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

package com.mytwitter.APIcall;

import com.mytwitter.Models.MyTweet;
import com.mytwitter.Models.ResponeWrap;
import com.mytwitter.Models.Response;
import com.mytwitter.Models.TweetsIds;
import com.mytwitter.Models.testBean;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Manish on 3/18/2016.
 */
public interface RestInterface {

    //get All Tweets
    @GET("statuses/user_timeline.json")
    Call<List<MyTweet>> getMyTweetIds(@Query("user_id") long user_id);

    //get All Recent Tweets
    @GET("statuses/home_timeline.json")
    Call<List<MyTweet>> getHomeTweetIds();

    @GET("search?api-key=test")
    Call<ResponeWrap> getNewsData();

    @POST("webservice")
    Call<testBean> loadtest(@QueryMap HashMap<String, String> params);
}
