package com.tinyblack.framework.controller;

import java.util.HashMap;

/**
 * @author yubiao
 */
public class ClickController {
    private static final String DEFAULT = "default";
    /**
     * 两次点击间隔不能少于500ms
     */
    private static final int MIN_DELAY_TIME = 500;
    private static final HashMap<String, Long> CLICK_DATA = new HashMap<>();

    public static boolean isFastClick(String tag, int coolDownTime) {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        Long lastTime = CLICK_DATA.get(tag);
        if (lastTime == null) {
            lastTime = 0L;
        }
        if ((currentClickTime - lastTime) >= coolDownTime) {
            flag = false;
        }
        CLICK_DATA.put(tag, currentClickTime);
        return flag;
    }

    public static boolean isFastClick() {
        return isFastClick(DEFAULT);
    }

    public static boolean isFastClick(String tag) {
        return isFastClick(tag, MIN_DELAY_TIME);
    }

}
