package com.tinyblack.framework.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;

/**
 *
 * @author liuj
 * @date 2017/7/13
 */

public class AndroidComponentUiTools {

    private static long lastClickTime;

    /**
     * 防止重复点击
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        return isFastClick(500);
    }

    /**
     * 防止重复点击
     *
     * @param timeDis 被视为多次连点的时间差
     * @return
     */
    public synchronized static boolean isFastClick(long timeDis) {
        long time = System.currentTimeMillis();
        if (time > lastClickTime && time - lastClickTime < timeDis) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @SuppressLint("ObsoleteSdkInt")
    public static void hideNavigateBar(final View view) {
        if (view == null){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            //布局位于状态栏下方
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            //全屏
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            //隐藏导航栏
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    if (Build.VERSION.SDK_INT >= 19) {
                        uiOptions |= 0x00001000;
                    } else {
                        uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        view.setSystemUiVisibility(uiOptions);
                    }
                }
            });
        }
    }
}
