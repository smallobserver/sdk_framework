package com.tinyblack.framework.controller;


import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件地址管理类
 *
 * @author yubiao
 */
public class FilePathController {

    private static String PATH_LOG_CHILD = "/Log/";
    private static String PATH_File = "/FileCache/";


    /**
     * 通用 cache 地址
     */
    public static String getCommonCache(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 外部储存可用
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DFCache";
        } else {
            return context.getCacheDir().getAbsolutePath() + "/DFCache";
        }
    }


    /**
     * log存放地址
     */
    public static String getLogCacheFile(Context context) {
        String directoryPath = getCommonCache(context) + PATH_LOG_CHILD;
        checkFile(new File(directoryPath));
        return directoryPath;
    }

    /**
     * 文件缓存地址
     *
     * @param context
     * @return
     */
    public static String getFileCache(Context context) {
        String directoryPath = getCommonCache(context) + PATH_File;
        checkFile(new File(directoryPath));
        return directoryPath;
    }

    /**
     * 获取数据库地址
     */
    public static String getDatabaseFile(Context context) {
        File dir = new File(context.getFilesDir().getAbsolutePath());
        checkFile(dir);
        return dir.getAbsolutePath();
    }

    private static void checkFile(File file) {
        //判断文件目录是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
