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

/**
 * Created by Manish on 3/17/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Extends the TextView widget to use custom fonts.
 */
public class PrettyTextView extends TextView {
    public PrettyTextView(Context context) {
        super(context);

        if (!isInEditMode())
            init();
    }

    public PrettyTextView(Context context, AttributeSet attributes) {
        super(context, attributes);

        if (!isInEditMode())
            init();
    }

    public PrettyTextView(Context context, AttributeSet attributes, int defStyle) {
        super(context, attributes, defStyle);

        if (!isInEditMode())
            init();
    }

    @Override
    public void setTypeface(Typeface typeface) {
        super.setTypeface(typeface);
    }

    private void init() {
        Typeface typeface = FontsHelper.loadFont(getContext(), FontsHelper.FONT_ROBOTO_REGULAR);
        setTypeface(typeface);
    }
}
