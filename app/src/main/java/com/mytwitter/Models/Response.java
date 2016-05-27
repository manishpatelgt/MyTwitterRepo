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
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 4/6/2016.
 */
public class Response {

    @Expose
    @SerializedName("status")
    String status;

    public String getstatus() {
        return this.status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    @Expose
    public List<Result> results = new ArrayList<Result>();

    public  List<Result> getResults () {
        return results ;
    }

    public void setResults(ArrayList<Result> results ) {
        this.results = results ;
    }

    //Make getter and setter for each and every fields in your JSON that you want

}
