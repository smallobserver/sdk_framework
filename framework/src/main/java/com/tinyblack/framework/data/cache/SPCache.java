package com.tinyblack.framework.data.cache;

/**
 * sp缓存,带sp
 * author: liutao
 * date: 2016/8/11.
 */
public interface SPCache {

    void putApply(String key, Object value);

    void put(String key, Object value);

    long getLong(String key, long defaultValue);

    String getString(String key, String defaultValue);

    boolean getBoolean(String key);

    void remove(String key);
}
