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

package com.mytwitter.Models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 3/18/2016.
 */
public class TweetsIds {

    @Expose
    private List<MyTweet> items = new ArrayList<MyTweet>();

    /**
     *
     * @return
     * The MyTweet
     */
    public  List<MyTweet> getItems() {
        return items;
    }

    /**
     *
     * @param Items
     * The MyTweet
     */
    public void setItems(ArrayList<MyTweet> Items) {
        this.items = Items;
    }
}
