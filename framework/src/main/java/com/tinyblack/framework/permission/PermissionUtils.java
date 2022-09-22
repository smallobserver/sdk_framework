package com.tinyblack.framework.permission;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


/**
 * @author yubiao
 */
public class PermissionUtils {
    /**
     * 一次性申请一个 或者多个权限
     * 只要其中有一个没有通过,返回为未通过
     *
     * @param activity
     * @param listener
     * @param permissions
     */
    public static void permissionApply(FragmentActivity activity, PermissionListener listener, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onAllowPermission();
            }
            return;
        }
        RxPermissions rxPermissions = new RxPermissions(activity);
        apply(listener, rxPermissions, permissions);
    }


    @SuppressLint("CheckResult")
    private static void apply(final PermissionListener listener, RxPermissions rxPermissions, String[] permissions) {
        rxPermissions
                .request(permissions)
                .subscribe(new PermissionConsumer(listener), new PermissionErrorConsumer(listener));
    }


    /**
     * 跳转至权限设置页面
     *
     * @param activity
     */
    public static void goPermissionSet(Activity activity) {
        String name = Build.MANUFACTURER;
        switch (name) {
            case "HUAWEI":
                goHuaWeiManager(activity);
                break;
            case "vivo":
                goVivoManager(activity);
                break;
            case "OPPO":
                goOppoManager(activity);
                break;
            case "Coolpad":
                goCoolpadManager(activity);
                break;
            case "Meizu":
                goMeizuManager(activity);
                break;
            case "Xiaomi":
                goXiaoMiManager(activity);
                break;
            case "samsung":
                goSangXinManager(activity);
                break;
            case "Sony":
                goSonyManager(activity);
                break;
            case "LG":
                goLGManager(activity);
                break;
            default:
                goIntentSetting(activity);
                break;
        }
    }

    private static void goLGManager(Activity activity) {
        try {
            Intent intent = new Intent(activity.getPackageName());
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goIntentSetting(activity);
        }
    }

    private static void goSonyManager(Activity activity) {
        try {
            Intent intent = new Intent(activity.getPackageName());
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goIntentSetting(activity);
        }
    }

    private static void goHuaWeiManager(Activity activity) {
        try {
            Intent intent = new Intent(activity.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goIntentSetting(activity);
        }
    }

    private static String getMiuiVersion(Activity activity) {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    private static void goXiaoMiManager(Activity activity) {
        String rom = getMiuiVersion(activity);
        Intent intent = new Intent();
        if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", activity.getPackageName());
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", activity.getPackageName());
        } else {
            goIntentSetting(activity);
        }
    }

    private static void goMeizuManager(Activity activity) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", activity.getPackageName());
            activity.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            goIntentSetting(activity);
        }
    }

    private static void goSangXinManager(Activity activity) {
        //三星4.3可以直接跳转
        goIntentSetting(activity);
    }

    private static void goIntentSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void goOppoManager(Activity activity) {
//        doStartApplicationWithPackageName("com.coloros.safecenter", activity);
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivity(localIntent);
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     * startActivity(open);
     */
    private static void goCoolpadManager(Activity activity) {
        doStartApplicationWithPackageName("com.yulong.android.security:remote", activity);
      /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    private static void goVivoManager(Activity activity) {
        doStartApplicationWithPackageName("com.bairenkeji.icaller", activity);
     /*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }

    private static void doStartApplicationWithPackageName(String packagename, Activity activity) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = activity.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = activity.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        System.out.println("resolve info List" + resolveinfoList.size());
        for (int i = 0; i < resolveinfoList.size(); i++) {
            System.out.println("PermissionPageManager" + resolveinfoList.get(i).activityInfo.packageName + resolveinfoList.get(i).activityInfo.name);
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityName]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            try {
                activity.startActivity(intent);
            } catch (Exception e) {
                goIntentSetting(activity);
                e.printStackTrace();
            }
        }
    }

    public static void goLocationService(@NotNull Context context) {
        final Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
