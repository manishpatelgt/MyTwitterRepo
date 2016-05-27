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

import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by Manish on 3/18/2016.
 */
public class RetrofitConverter implements Converter {
    private Converter mSerializer;
    private Converter mDeserializer;

    public RetrofitConverter(Converter serializer) {
        mSerializer = serializer;
        mDeserializer = serializer;
    }

    public RetrofitConverter(Converter serializer, Converter deserializer) {
        mSerializer = serializer;
        mDeserializer = deserializer;
    }


    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        return mDeserializer.fromBody(body, type);
    }

    @Override
    public TypedOutput toBody(Object object) {
        return mSerializer.toBody(object);
    }
}