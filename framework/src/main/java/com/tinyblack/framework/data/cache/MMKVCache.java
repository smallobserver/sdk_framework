package com.tinyblack.framework.data.cache;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

import io.reactivex.rxjava3.annotations.Nullable;

/**
 * 替换原ACache,使用腾讯开源 MMKV 来进行底层效率增加
 */
public class MMKVCache {

    private final static String TAG = "Cache";
    private static MMKVCache mInstance;

    private MMKV mCache;
    private Gson gson;


    /**
     * 存储路径
     *
     * @param dirPath
     */
    public static void init(Context context, String dirPath) {
        MMKV.initialize(context, dirPath);
    }


    public static MMKVCache getDefaultInstance() {
        if (mInstance == null) {
            mInstance = new MMKVCache(MMKV.mmkvWithID(TAG));
        }
        return mInstance;
    }

    public static MMKVCache getInstanceByID(String id) {
        return new MMKVCache(MMKV.mmkvWithID(id));
    }

    private MMKVCache(@Nullable MMKV mCache) {
        this.mCache = mCache;
        gson = new Gson();
    }

    // =======================================
    // ============ String数据 读写 ==============
    // =======================================
    public String getAsString(String key) {
        if (mCache == null) {
            return null;
        }
        String data = mCache.decodeString(key, "");
//        if (Utils.isDue(data)) {
//            return Utils.clearDateInfo(data);
//        } else {
//            mCache.remove(key);
//            return null;
//        }
        return data;
    }

    public boolean getAsBoolean(String key, boolean b) {
        if (mCache == null) {
            return false;
        }

        return mCache.decodeBool(key, b);
    }

    public int getAsInt(String key, int b) {
        if (mCache == null) {
            return 0;
        }

        return mCache.decodeInt(key, b);
    }

    public long getAsLong(String key, long b) {
        if (mCache == null) {
            return 0;
        }

        return mCache.decodeLong(key, b);
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value) {
        if (mCache != null) {
            mCache.encode(key, value);
        }
    }

    public void put(String key, boolean value) {
        if (mCache != null) {
            mCache.encode(key, value);
        }
    }

    public void put(String key, int value) {
        if (mCache != null) {
            mCache.encode(key, value);
        }
    }

    public void put(String key, long value) {
        if (mCache != null) {
            mCache.encode(key, value);
        }
    }

    /**
     * 存入对象
     *
     * @param key
     * @param object
     */
    public void put(String key, Object object) {
        if (mCache != null) {
            String json = gson.toJson(object);
            mCache.encode(key, json);
        }
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的String数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, String value, int saveTime) {
        if (mCache != null) {
            mCache.encode(key, Utils.newStringWithDateInfo(saveTime, value));
        }
    }


    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的JSON数据
     */
    public void put(String key, JSONObject value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的JSONObject数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, JSONObject value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * 读取JSONObject数据
     *
     * @param key
     * @return JSONObject数据
     */
    public JSONObject getAsJSONObject(String key) {
        String JSONString = getAsString(key);
        try {
            JSONObject obj = new JSONObject(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============ JSONArray 数据 读写 =============
    // =======================================

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的JSONArray数据
     */
    public void put(String key, JSONArray value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的JSONArray数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, JSONArray value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * 读取JSONArray数据
     *
     * @param key
     * @return JSONArray数据
     */
    public JSONArray getAsJSONArray(String key) {
        String JSONString = getAsString(key);
        try {
            JSONArray obj = new JSONArray(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        if (mCache != null) {
            mCache.encode(key, value);
        }
    }

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, byte[] value, int saveTime) {
        put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
    }

    /**
     * 获取 byte 数据
     *
     * @param key
     * @return byte 数据
     */
    public byte[] getAsBinary(String key) {
        if (mCache == null) {
            return null;
        }
        byte[] byteArray = mCache.decodeBytes(key);
        if (!Utils.isDue(byteArray)) {
            return Utils.clearDateInfo(byteArray);
        } else {
            mCache.remove(key);
            return null;
        }

    }

    public <T> T getAsGson(String key, Class<T> classOfT) {
        String cache = getAsString(key);
        if (TextUtils.isEmpty(cache)) {
            return null;
        }
        try {
            return gson.fromJson(cache, classOfT);
        } catch (Exception e) {
            return null;
        }

    }

    public <T> T getAsGson(String key, Type type) {
        String cache = getAsString(key);
        if (TextUtils.isEmpty(cache)) {
            return null;
        }
        try {
            return (T) gson.fromJson(cache, type);
        } catch (Exception e) {
            return null;
        }

    }

    public void putAsGson(String key, Object value) {
        String cache = gson.toJson(value);
        put(key, cache);
    }

    public void putAsGson(String key, Object value, int time) {
        String cache = gson.toJson(value);
        put(key, cache, time);
    }

    /**
     * 移除某个key
     *
     * @param key
     * @return 是否移除成功
     */
    public void remove(String key) {
        if (mCache != null) {
            mCache.remove(key);
        }
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        if (mCache != null) {
            mCache.clear();
        }
    }

    /**
     * @author 杨福海（michael） www.yangfuhai.com
     * @version 1.0
     * @title 时间计算工具类
     */
    private static class Utils {

        /**
         * 判断缓存的String数据是否到期
         *
         * @param str
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(String str) {
            return isDue(str.getBytes());
        }

        /**
         * 判断缓存的byte数据是否到期
         *
         * @param data
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(byte[] data) {
            String[] strs = getDateInfoFromDate(data);
            if (strs != null && strs.length == 2) {
                String saveTimeStr = strs[0];
                while (saveTimeStr.startsWith("0")) {
                    saveTimeStr = saveTimeStr
                            .substring(1, saveTimeStr.length());
                }
                long saveTime = Long.valueOf(saveTimeStr);
                long deleteAfter = Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
                    return true;
                }
            }
            return false;
        }

        private static String newStringWithDateInfo(int second, String strInfo) {
            return createDateInfo(second) + strInfo;
        }

        private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
            byte[] data1 = createDateInfo(second).getBytes();
            byte[] retdata = new byte[data1.length + data2.length];
            System.arraycopy(data1, 0, retdata, 0, data1.length);
            System.arraycopy(data2, 0, retdata, data1.length, data2.length);
            return retdata;
        }

        private static String clearDateInfo(String strInfo) {
            if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
                strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1,
                        strInfo.length());
            }
            return strInfo;
        }

        private static byte[] clearDateInfo(byte[] data) {
            if (hasDateInfo(data)) {
                return copyOfRange(data, indexOf(data, mSeparator) + 1,
                        data.length);
            }
            return data;
        }

        private static boolean hasDateInfo(byte[] data) {
            return data != null && data.length > 15 && data[13] == '-'
                    && indexOf(data, mSeparator) > 14;
        }

        private static String[] getDateInfoFromDate(byte[] data) {
            if (hasDateInfo(data)) {
                String saveDate = new String(copyOfRange(data, 0, 13));
                String deleteAfter = new String(copyOfRange(data, 14,
                        indexOf(data, mSeparator)));
                return new String[]{saveDate, deleteAfter};
            }
            return null;
        }

        private static int indexOf(byte[] data, char c) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] == c) {
                    return i;
                }
            }
            return -1;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (newLength < 0) {
                throw new IllegalArgumentException(from + " > " + to);
            }
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0,
                    Math.min(original.length - from, newLength));
            return copy;
        }

        private static final char mSeparator = ' ';

        private static String createDateInfo(int second) {
            String currentTime = System.currentTimeMillis() + "";
            while (currentTime.length() < 13) {
                currentTime = "0" + currentTime;
            }
            return currentTime + "-" + second + mSeparator;
        }


    }
}
