package com.tinyblack.framework.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.tinyblack.framework.base.BaseActivity;
import com.tinyblack.framework.log.TLog;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;


/**
 * Created by ljd on 17-8-3.
 */

public class SystemUtils {

    public static void hideStatusUI(Window window) {
        WindowManager.LayoutParams attrs = window
                .getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attrs);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = window.getDecorView();
        int mSystemUiVisibility = View.INVISIBLE;
        window.getDecorView().setSystemUiVisibility(mSystemUiVisibility);
    }

    public static void showStatusUI(Window window) {
        WindowManager.LayoutParams attrs = window
                .getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attrs);
        if (window.getAttributes().flags == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        View decorView = window.getDecorView();
        int mSystemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(mSystemUiVisibility);
    }

    /**
     * 隐藏虚拟按键
     *
     * @param activity
     */
    public static void hideNavigateBar(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        hideNavigateBar(activity.getWindow());
//
    }

    public static void hideNavigateBar(Window window) {
        if (window == null) {
            return;
        }
        final View decorView = window.getDecorView();
        //隐藏虚拟按键，并且全屏
//        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//            decorView.setSystemUiVisibility(View.GONE);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            //for new api versions.
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
        int mSystemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        decorView.setSystemUiVisibility(mSystemUiVisibility);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (isOrientationPortrait(decorView.getContext())) {
                    return;
                }
                int mSystemUiVisibility = decorView.getSystemUiVisibility();

                int uiOptions = mSystemUiVisibility;
                uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
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
                if (uiOptions == mSystemUiVisibility) {
                    return;
                }
                TLog.e("uiOptions=" + uiOptions);
                decorView.setSystemUiVisibility(uiOptions);
            }
        });
    }

    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    public static boolean isOrientationPortrait(Context context) {
        if (context == null) {
            return false;
        }
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 返回当前屏幕是否为横屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为横屏时返回true, 否则返回false。
     */
    public static boolean isOrientationLandscape(Context context) {
        if (context == null) {
            return false;
        }
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean checkSystemGravity(Context context) {
        try {
            int systemGravity = Settings.System.getInt(context.getApplicationContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
            if (systemGravity == 1) {
                return true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void exitProcess(Context context) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).finish();
        }
        Disposable subscribe = Observable.just(0).delay(300, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Process.killProcess(Process.myPid());
                System.exit(0);
                throw new RuntimeException("System.exit returned normally, while it was supposed to halt JVM.");
            }
        });
    }
}
