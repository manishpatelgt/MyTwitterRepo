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

package com.mytwitter.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Manish on 5/7/2016.
 */
public class MyResultReceiver extends BroadcastReceiver {

    private static Logger logger = LoggerFactory.getLogger("c*.m*.r*.MyResult*");

    @Override
    public void onReceive(Context context, Intent intent) {
        logger.debug("inside MyResultReceiver");
        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
            // success
            final Long tweetId = intent.getExtras().getLong(TweetUploadService.EXTRA_TWEET_ID);
            Toast.makeText(context, "Tweet posted Successfully.", Toast.LENGTH_LONG).show();
        } else {
            // failure
            final Intent retryIntent = intent.getExtras().getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
            Toast.makeText(context, "Tweet posted Failed.", Toast.LENGTH_LONG).show();
        }
    }
}