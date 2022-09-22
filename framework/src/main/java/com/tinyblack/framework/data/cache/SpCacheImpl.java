package com.tinyblack.framework.data.cache;

import android.content.Context;

/**
 * Created by liutao on 30/06/2017.
 */

public class SpCacheImpl implements SPCache{
    private final SPStorageUtil spStorageUtil;
    public SpCacheImpl(Context context){
        spStorageUtil=SPStorageUtil.get(context);
    }
    @Override
    public void putApply(String key, Object value) {
        spStorageUtil.putApply(key,value);
    }

    @Override
    public void put(String key, Object value) {
        spStorageUtil.putApply(key,value);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return spStorageUtil.getLong(key,defaultValue);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return spStorageUtil.getAsString(key,defaultValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return spStorageUtil.getAsBoolean(key);
    }

    @Override
    public void remove(String key) {
        spStorageUtil.remove(key);
    }
}
