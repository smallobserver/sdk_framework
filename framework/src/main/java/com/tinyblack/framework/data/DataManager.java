package com.tinyblack.framework.data;

import android.content.Context;

/**
 * the dataManager
 * every module should get the data from here
 */

public class DataManager {

    private static DataManager INSTANCE;


    private DataManager(Context context) {
        //数据缓存初始化
        DataCache.init(context);
    }

    public static DataManager instance() {
        if (INSTANCE == null) {
            throw new RuntimeException("must init before use the instance");
        }
        return INSTANCE;
    }

    public static synchronized void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DataManager(context);
        }
    }


}
