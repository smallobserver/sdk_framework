package com.tinyblack.framework.util.systembar;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.tinyblack.framework.log.TLog;
import com.tinyblack.framework.util.SystemUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Eyes {


    private static void setStatusBarColor(Activity activity, int statusColor) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Window window = activity_play.getWindow();
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            );
//            window.setStatusBarColor(statusColor);
//
//            //fitsSystemWindow 为 false, 不预留系统栏位置.
//            ViewGroup mContentView = (ViewGroup) activity_play.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
//            View mChildView = mContentView.getChildAt(0);
//            if (mChildView != null) {
//                mChildView.setFitsSystemWindows(true);
//                ViewCompat.requestApplyInsets(mChildView);
//            }
//        } else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            EyesLollipop.setStatusBarColor(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            EyesKitKat.setStatusBarColor(activity, statusColor);
        }
    }

    public static boolean translucentStatusBar(Activity activity) {
        return translucentStatusBar(activity, false);
    }

    public static boolean translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        return translucentStatusBar(activity, hideStatusBarBackground, canToggleStatusBar(activity));
    }

    public static boolean translucentStatusBar(Activity activity, boolean hideStatusBarBackground, boolean toggleStatusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            EyesLollipop.translucentStatusBar(activity, hideStatusBarBackground);
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (toggleStatusBar) {
                EyesKitKat.translucentStatusBar(activity);
                return true;
            } else {
                //BUG 设置颜色和设置沉浸式冲突,设置颜色后位置无法还原
//                EyesKitKat.translucentStatusBar(activity_play);
            }
        }
        return false;
    }

    /**
     * 是否可以切换颜色及沉浸式
     *
     * @return
     */
    public static boolean canToggleStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMIUI(activity) || isFlyme(activity)) {
                return true;
            }
        }
        return false;
    }

//    public static void setStatusBarColorForCollapsingToolbar(@NonNull Activity activity_play, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout,
//                                                             Toolbar toolbar, @ColorInt int statusColor) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            EyesLollipop.setStatusBarColorForCollapsingToolbar(activity_play, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            EyesKitKat.setStatusBarColorForCollapsingToolbar(activity_play, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
//        }
//    }

    public static void setStatusBarLightMode(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            boolean isLightColor = isLightColor(color);
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                //5.0以上版本切换状态栏颜色之前清空透明状态
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }

            }

            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (MIUISetStatusBarLightMode(activity, isLightColor) || FlymeSetStatusBarLightMode(activity, isLightColor)) {
                //设置状态栏为指定颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (SystemUtils.isOrientationPortrait(activity)) {
                            ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                            View mChildView = mContentView.getChildAt(0);
//                            boolean isFitsSystemWindows = mChildView.getFitsSystemWindows();
//                            PluLog.d("isFitsSystemWindows=" + isFitsSystemWindows);
                            if (mChildView != null) {
                                mChildView.setFitsSystemWindows(true);
                                ViewCompat.requestApplyInsets(mChildView);
                            }
                        }
                        window.setStatusBarColor(color);
                    } else {
                        window.setStatusBarColor(color);
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                    //调用修改状态栏颜色的方法
                    setStatusBarColor(activity, color);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (color == Color.WHITE) {//不支持修改字体颜色,采用默认黑色背景
                    color = Color.BLACK;
                }
                //添加Flag把状态栏设为可绘制模式
                window.setStatusBarColor(color);
                //fitsSystemWindow 为 false, 不预留系统栏位置.
                ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    mChildView.setFitsSystemWindows(false);
                    ViewCompat.requestApplyInsets(mChildView);
                }

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                //会导致默认背景被去除
//                activity_play.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//影响切换横屏隐藏状态栏
                window.setStatusBarColor(color);

                // 如果亮色，设置状态栏文字为黑色
                if (isLightColor(color)) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }

                //fitsSystemWindow 为 false, 不预留系统栏位置.
                ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    mChildView.setFitsSystemWindows(true);
                    ViewCompat.requestApplyInsets(mChildView);
                }
            }
        }
    }

    private static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

//    public static void setStatusBarLightForCollapsingToolbar(Activity activity_play, AppBarLayout appBarLayout,
//                                                             CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusBarColor) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            EyesLollipop.setStatusBarWhiteForCollapsingToolbar(activity_play, appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            EyesKitKat.setStatusBarWhiteForCollapsingToolbar(activity_play, appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
//        }
//    }

    public static boolean isMIUI(Activity activity) {
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFlyme(Activity activity) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
//            darkFlag.setAccessible(true);
//            meizuFlags.setAccessible(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * MIUI的沉浸支持透明白色字体和透明黑色字体
     * https://dev.mi.com/console/doc/detail?pId=1159
     */
    static boolean MIUISetStatusBarLightMode(Activity activity, boolean darkmode) {
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            Class<? extends Window> clazz = activity.getWindow().getClass();
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     */
    static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }

    static void setContentTopPadding(Activity activity, int padding) {
        ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    static int getPxFromDp(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    /**
     * 导航栏，状态栏隐藏
     *
     * @param activity
     */
    public static void hideNavigationBarStatusBar(Activity activity) {
        final Window window = activity.getWindow();
        final View decorView = window.getDecorView();

        WindowManager.LayoutParams attrs = window
                .getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attrs);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
            if (MIUISetStatusBarLightMode(activity, true) || FlymeSetStatusBarLightMode(activity, true)) {
                //设置状态栏为指定颜色
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//5.0以下移除适配用的view
                    EyesKitKat.removeStatusBar(activity);
                }
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //设置Window为透明
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(Color.TRANSPARENT);
                ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    mChildView.setFitsSystemWindows(false);
                    ViewCompat.requestApplyInsets(mChildView);
                }
            }
        }
        hideNavigateBar(decorView);
    }

    public static void hideNavigateBar(final View decorView) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (decorView == null) return;
                if (SystemUtils.isOrientationPortrait(decorView.getContext())) {
                    return;
                }
                int mSystemUiVisibility = decorView.getSystemUiVisibility();

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
                if (uiOptions == mSystemUiVisibility) {
                    return;
                }
                TLog.e("uiOptions=" + uiOptions);
                decorView.setSystemUiVisibility(uiOptions);
            }
        });
    }

    /**
     * 设置状态栏字体高亮
     *
     * @param window
     * @param lightMode
     */
    public static void setStatusBarLightMode(Window window, boolean lightMode) {
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorView);
            wic.setAppearanceLightStatusBars(lightMode);
        }
    }
}