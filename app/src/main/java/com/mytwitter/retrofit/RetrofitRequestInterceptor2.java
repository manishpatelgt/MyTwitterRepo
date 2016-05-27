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

import com.mytwitter.Utils.Consts;
import com.mytwitter.Utils.SignatureMethod;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.OAuth;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Manish on 3/19/2016.
 */
public class RetrofitRequestInterceptor2 implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();

        String oauth_signature_method = "HMAC-SHA1";
        String oauth_consumer_key = "TMr0RTusaWKrhnsTjRHpKqfBA";
        String oauth_token= authToken.token;
        String uuid_string = UUID.randomUUID().toString();
        uuid_string = uuid_string.replaceAll("-", "");
        String oauth_nonce = uuid_string; // any relatively random alphanumeric string will work here. I used UUID minus "-" signs
        Date now = Calendar.getInstance().getTime();
        String oauth_timestamp = (new Long(now.getTime()/1000)).toString(); // get current time in milliseconds, then divide by 1000 to get seconds
        // I'm not using a callback value. Otherwise, you'd need to include it in the parameter string like the example above
        // the parameter string must be in alphabetical order
        String parameter_string = "oauth_consumer_key=" + oauth_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + "&oauth_timestamp=" + oauth_timestamp + "&oauth_version=1.0";
        System.out.println("parameter_string=" + parameter_string);
        String signature_base_string = "GET&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fstatuses%2Fhome_timeline.json&" + URLEncoder.encode(parameter_string, "UTF-8");
        System.out.println("signature_base_string=" + signature_base_string);
        String oauth_signature = "";

        try {
            oauth_signature = computeSignature(signature_base_string, "3yasdfasmyconsumersecretfasd53&");  // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
            System.out.println("oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8"));
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*String authorization_header_string =
                " oauth_consumer_key=\"" + oauth_consumer_key+
                "\", oauth_nonce=\"" + oauth_nonce +
                "\", oauth_signature=\""+URLEncoder.encode(oauth_signature, "UTF-8")+ "\""+
                " oauth_signature_method=\"HMAC-SHA1\""+
                "\", oauth_timestamp=\""+ oauth_timestamp +
                "\", oauth_token=\""+oauth_token+
                        "\" OAuth oauth_version=\"1.0\"";*/

        String authorization_header_string="OAuth oauth_version=\"1.0\","+
                " oauth_signature_method=\"HMAC-SHA1\""+
                ", oauth_nonce=\"" + oauth_nonce +
                "\", oauth_timestamp=\""+ oauth_timestamp +
                "\", oauth_consumer_key=\"" + oauth_consumer_key+
                "\", oauth_token=\""+oauth_token+
                "\", oauth_signature=\""+URLEncoder.encode(oauth_signature, "UTF-8")+ "\"";

        /*String authorization_header_string="OAuth oauth_version=\"1.0\","+
                " oauth_signature_method=\"HMAC-SHA1\""+
                ", oauth_nonce=\"" + oauth_nonce +
                "\", oauth_timestamp=\""+ oauth_timestamp +
                "\", oauth_consumer_key=\"" + oauth_consumer_key+
                "\", oauth_token=\""+oauth_token+
                "\", oauth_signature=\""+URLEncoder.encode(oauth_signature, "UTF-8")+ "\"";*/


        //String authorization_header_string = "OAuth oauth_consumer_key=\"" + oauth_consumer_key + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" +
                //oauth_timestamp + "\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\"" + URLEncoder.encode(oauth_signature, "UTF-8") + "\"";

        System.out.println("authorization_header_string=" + authorization_header_string);

        // Customize the request
        Request request = original.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", authorization_header_string)
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

    private String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {

        SecretKey secretKey = null;

        byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");

        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
    }

}
