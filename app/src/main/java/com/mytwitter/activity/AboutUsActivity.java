
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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mytwitter.R;
import com.mytwitter.Utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Manish on 5/7/2016.
 */
public class AboutUsActivity extends AppCompatActivity {

    private static Logger logger = LoggerFactory.getLogger("c*.m*.AboutUsAct*");
    private TextView versionText,LinkText,relText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_action_twitter);

        versionText=(TextView) findViewById(R.id.version);
        System.out.println("version: " + Utils.getApplicationVersionName(this));
        versionText.setText("Version " + Utils.getApplicationVersionName(this));

        relText = (TextView)findViewById(R.id.relText);
        relText.setText(getResources().getString(R.string.release_notes));

        LinkText=(TextView)findViewById(R.id.link);
        LinkText.setClickable(true);

        String mystring=new String("Feedback");
        SpannableString content = new SpannableString(mystring);
        //content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.brown3)), 0, mystring.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        LinkText.setText(content);

        LinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mdp3030@gmail.com"});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MyOwnTwitter App Feedback");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                sendIntent.setType("plain/text");
                startActivity(sendIntent);
            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // Use left swipe when user presses hardware back button
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
