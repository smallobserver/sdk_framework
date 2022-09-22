package com.tinyblack.framework.data.cache;


import android.util.LruCache;

/**
 * @author liutao
 * @date 30/06/2017
 */

public class MemoryCacheImpl implements MemoryCache {

    private static MemoryCache mMemoryCache;
    private static LruCache<String, Object> mLruCache;

    private MemoryCacheImpl() {
        //32M
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        //1M数据空间
        final int cacheSize = maxMemory / 32;
        mLruCache = new LruCache<>(cacheSize);
    }

    public static MemoryCache getInstance() {
        synchronized (MemoryCache.class) {
            if (mMemoryCache == null) {
                mMemoryCache = new MemoryCacheImpl();
            }
            return mMemoryCache;
        }
    }

    @Override
    public boolean put(String key, Object value) {
        try {
            mLruCache.put(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object get(String key) {
        try {
            return mLruCache.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeApplyList(String[] keys) {
        for (String key : keys) {
            mLruCache.remove(key);
        }
    }
}
