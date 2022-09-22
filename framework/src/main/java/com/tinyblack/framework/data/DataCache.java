package com.tinyblack.framework.data;

import android.content.Context;

import com.tinyblack.framework.data.cache.DiskCache;
import com.tinyblack.framework.data.cache.DiskCacheImpl;
import com.tinyblack.framework.data.cache.MemoryCache;
import com.tinyblack.framework.data.cache.MemoryCacheImpl;
import com.tinyblack.framework.data.cache.SPCache;
import com.tinyblack.framework.data.cache.SpCacheImpl;


/**
 * the cache manager
 */
public class DataCache {

    private static DataCache INSTANCE;

    private final MemoryCache memoryCache;
    private final SpCacheImpl spCache;
    private final DiskCacheImpl diskCache;

    private DataCache(Context context) {
        memoryCache = MemoryCacheImpl.getInstance();
        spCache = new SpCacheImpl(context);
        diskCache = new DiskCacheImpl(context);
    }

    public static synchronized void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DataCache(context);
        }
    }

    public static DataCache instance() {
        return INSTANCE;
    }

    public DiskCache getDiskCache() {
        return diskCache;
    }

    public MemoryCache getMemoryCache() {
        return memoryCache;
    }

    public SPCache getSpCache() {
        return spCache;
    }
}
