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

import android.util.Base64;

import com.mytwitter.Utils.Consts;
import com.mytwitter.Utils.TimeHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Manish on 3/18/2016.
 */
public class RetrofitRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        String finalHeader="OAuth oauth_version=\"1.0\", oauth_signature_method=\"HMAC-SHA1\", oauth_nonce=\"bb364941094c42b7e4bbfbff0bc44cc7\", oauth_timestamp=\"1460964997\", oauth_consumer_key=\"TMr0RTusaWKrhnsTjRHpKqfBA\", oauth_token=\"406136056-ZlxXkPEhE9AUfZ2D5QIC0JvVUjAa4OGHSPpMeVzo\", oauth_signature=\"0iJ549xNRDy%2FvmDn09vYnW8EmDQ%3D\"";

        // Customize the request
        Request request = original.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", finalHeader)
                .method(original.method(), original.body())
                .build();

        Response response = chain.proceed(request);

        // Customize or return the response
        return response;
    }

    /*@Override
    public void intercept(RequestFacade request) {

        //request.addHeader("Accept", "application/json");
        StringBuilder final_header = new StringBuilder();
        final_header.append("OAuth oauth_version=\"1.0\", ");
        final_header.append("oauth_signature_method=\"HMAC-SHA1\", ");
        final_header.append("oauth_nonce="+ "\"" +Consts.oauth_nonce + "\", ");
        final_header.append("oauth_timestamp="+ "\"" + "1458286423" + "\", ");
        final_header.append("oauth_consumer_key="+ "\"" +Consts.Consumer_key + "\", ");
        final_header.append("oauth_token="+ "\"" +Consts.Access_token + "\", ");
        final_header.append("oauth_signature=" + "\"" + Consts.oAuth_signature + "\"");

        String finalHeader="OAuth oauth_version=\"1.0\", oauth_signature_method=\"HMAC-SHA1\", oauth_nonce=\"tHCWJeO0oYeUv9P\", oauth_timestamp=\"1458291200\", oauth_consumer_key=\"TMr0RTusaWKrhnsTjRHpKqfBA\", oauth_token=\"406136056-ZlxXkPEhE9AUfZ2D5QIC0JvVUjAa4OGHSPpMeVzo\", oauth_signature=\"5x%2FXi6jzrdDco7iR5NKcHFPvbVA%3D\"";
        request.addHeader("Authorization", finalHeader);

    }*/

}
