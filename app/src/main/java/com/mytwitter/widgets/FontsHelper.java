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

package com.mytwitter.widgets;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by Manish on 3/17/2016.
 */
public class FontsHelper {

        public static final String FONT_ROBOTO_BLACK = "roboto_black.ttf";
        public static final String FONT_ROBOTO_REGULAR = "roboto_regular.ttf";
       public static final String FONT_CAVIER = "CaviarDreams.ttf";

        private static final Hashtable<String, Typeface> fontsCache = new Hashtable<String, Typeface>();

        /**
         * Loads custom fonts from the /assets folder and caches them.
         *
         */
        public static Typeface loadFont(Context context, String fontName) {

            // lock to make sure that the font is added to the cache only once
            synchronized (fontsCache) {

                if (!fontsCache.containsKey(fontName)) {
                    // Save the font in the cache first
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
                    fontsCache.put(fontName, typeface);
                }

                return fontsCache.get(fontName);
            }
        }

}
