package com.tinyblack.framework.data.cache;

/**
 *
 * @date 2016/6/30
 */
public interface MemoryCache {

    boolean put(String key, Object value);

    Object get(String key);

    void removeApplyList(String[] keys);
}
