package com.google.codelabs.mdc.java.shrine.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

public final class DensityUtil {
    private static float sDensity = -1f;
    private static float sScaledDensity = -1f;

    private DensityUtil() {}

    public static float getDensity(Context context) {
        if (sDensity == -1) {
            sDensity = context.getResources().getDisplayMetrics().density;
        }
        return sDensity;
    }

    public static float getScaledDensity(Context context) {
        if (sScaledDensity == -1) {
            sScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        }
        return sScaledDensity;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        return (int) (spValue * getScaledDensity(context) + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / getScaledDensity(context) + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        Resources res = context.getResources();
        if (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return res.getDisplayMetrics().widthPixels;
        }
        return res.getDisplayMetrics().widthPixels + getNavigationBarHeight(context);
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels - getStatusBarHeight(context);
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNavigationBarHeight(Context context) {
        if (checkDeviceHasNavigationBar(context)) {
            int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
            if (rid != 0) {
                int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                return context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        // 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(context)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            return true;
        }
        return false;
    }
}
