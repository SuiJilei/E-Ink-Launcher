package cn.modificator.launcher;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import cn.modificator.launcher.floatball.FloatViewManager;

public class EInkAccessibilityService extends AccessibilityService {
    private static EInkAccessibilityService instance;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = this;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public static EInkAccessibilityService getInstance() {
        return instance;
    }
}
