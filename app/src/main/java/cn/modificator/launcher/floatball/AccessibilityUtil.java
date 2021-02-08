package cn.modificator.launcher.floatball;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by wangxiandeng on 2016/11/25.
 */

public class AccessibilityUtil {
    /**
     * 单击返回功能
     * @param service
     */
    public static void doBack(AccessibilityService service) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Log.e("INTER","INTER");
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    /**
     * 下拉打开通知栏
     * @param service
     */
    public static void doPullDown(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
        }
    }

    /**
     * 上拉返回桌面
     * @param service
     */
    public static void doPullUp(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        }
    }

    /**
     * 左右滑动打开多任务
     * @param service
     */
    public static void doLeftOrRight(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
        }
    }

    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }






    }