package com.tinyblack.framework.data.cache;

import android.content.Context;

import org.json.JSONArray;

import java.lang.reflect.Type;

/**
 * Created by liutao on 30/06/2017.
 */

public class DiskCacheImpl implements DiskCache {
    private final MMKVCache aCache;

    public DiskCacheImpl(Context context) {
        aCache = MMKVCache.getDefaultInstance();
    }

    @Override
    public <T> T getAsGson(String key, Class<T> classOfT) {
        return aCache.getAsGson(key, classOfT);
    }

    @Override
    public <T> T getAsGson(String key, Type type) {
        return aCache.getAsGson(key, type);
    }

    @Override
    public void putAsGson(String key, Object value) {
        aCache.putAsGson(key, value);
    }


    @Override
    public String getAsString(String key) {
        return aCache.getAsString(key);
    }

    @Override
    public String getAsString(String key, String def) {
        return aCache.getAsString(key);
    }

    @Override
    public void put(String key, JSONArray value, int time) {
        aCache.put(key, value, time);
    }

    @Override
    public void put(String key, String value) {
        aCache.put(key, value);
    }


    @Override
    public void putAsGson(String key, Object value, int time) {
        aCache.putAsGson(key, value, time);
    }

    @Override
    public void put(String key, String value, int saveTime) {
        aCache.put(key, value, saveTime);
    }


    @Override
    public void clearData(String key) {
        aCache.remove(key);
    }

    @Override
    public boolean getBoolean(Context ct, String key, boolean defValue) {
        return false;
    }

    @Override
    public boolean getBoolean(String key) {
        return false;
    }

    @Override
    public void clear() {
        aCache.clear();
    }


}
