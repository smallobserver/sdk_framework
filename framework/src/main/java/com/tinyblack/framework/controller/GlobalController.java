package com.tinyblack.framework.controller;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 做全局处理
 * 全局存在,持有context
 *
 * @author yubiao
 */
public class GlobalController {

    private static Context context;

    private static Gson gson;


    public static Gson getGson() {
        if (gson == null) {
            synchronized (GlobalController.class) {
                if (gson == null) {
                    gson = new GsonBuilder().setPrettyPrinting().create();
                }
            }
        }
        return gson;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        GlobalController.context = context;
    }
}
