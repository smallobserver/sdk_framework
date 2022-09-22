package com.tinyblack.framework.net.gson;/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");
    private HttpUrl httpUrl;
    GsonResponseBodyConverter(TypeAdapter<T> adapter, HttpUrl url) {
        this.adapter = adapter;
        this.httpUrl=url;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return adapter.fromJson(value.charStream());
        } catch (Exception e) {
            String url = httpUrl.url().toString();
            String hostName =httpUrl.host();
//            int hType =httpUrl.isHttps() ? Constant.Logger.HTTPS : Constant.Logger.HTTP;
//            EventBus.getDefault().post(new LoggerReq(hostName, url, hType));
            throw e;
        } finally {
            value.close();
        }
    }
}
