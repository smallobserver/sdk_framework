package com.tinyblack.framework.data.cache;

import android.content.Context;

import org.json.JSONArray;

import java.lang.reflect.Type;

/**
 * the disk cache
 *
 * @author liutao
 * @date 30/06/2017
 */
public interface DiskCache {
    <T> T getAsGson(String key, Class<T> classOfT);

    <T> T getAsGson(String key, Type type);

    void putAsGson(String key, Object value);

    String getAsString(String key);

    String getAsString(String key, String def);

    void put(String key, JSONArray value, int time);

    void put(String key, String value);

    void put(String key, String value, int saveTime);

    void putAsGson(String key, Object value, int time);

    void clearData(String key);

    boolean getBoolean(Context ct, String key, boolean defValue);

    boolean getBoolean(String key);

    void clear();


}
