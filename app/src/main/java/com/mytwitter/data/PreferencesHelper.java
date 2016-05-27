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

package com.mytwitter.data;

import android.content.SharedPreferences;
import com.mytwitter.application.TwitterApplication;

/**
 * Created by Manish on 12/15/2015.
 */
public class PreferencesHelper {


    private static final String IS_LOGIN_PREFS = "Is_Login";

    private static final String PREF_KEY_USER_ID = "UserId";
    private static final String PREF_KEY_USER_NAME = "UserName";

    public static Boolean getBoolean(String key, Boolean defValue) {
        return TwitterApplication.getPreferences().getBoolean(key, defValue);
    }

    public static void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = TwitterApplication.getPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Long getLong(String key, long defValue) {
        return TwitterApplication.getPreferences().getLong(key, defValue);
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = TwitterApplication.getPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }
    public static String getString(String key, String defValue) {
        return TwitterApplication.getPreferences().getString(key, defValue);
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = TwitterApplication.getPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defValue) {
        return TwitterApplication.getPreferences().getInt(key, defValue);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = TwitterApplication.getPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getUserName() {
        return getString(PREF_KEY_USER_NAME, "");
    }

    public static void setUserName(String userName) {
        putString(PREF_KEY_USER_NAME, userName);
    }

    public static boolean getLoginCheck() {
        return getBoolean(IS_LOGIN_PREFS, false);
    }

    public static void setLoginCheck(boolean tested) {
        putBoolean(IS_LOGIN_PREFS, tested);
    }

    public static long getUserID() {
        return getLong(PREF_KEY_USER_ID, 0);
    }

    public static void setUserID(long value) {
         putLong(PREF_KEY_USER_ID, value);
    }

}

