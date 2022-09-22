package com.tinyblack.framework.clean.base;


import com.tinyblack.framework.data.cache.DiskCache;
import com.tinyblack.framework.data.cache.MemoryCache;
import com.tinyblack.framework.data.cache.SPCache;

/**
 * 基类
 * =======
 */
public interface DataRepository {
    MemoryCache getMemoryCache();
    DiskCache getDiskCache();
    SPCache getSpCache();
}
